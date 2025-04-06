package services;

import dtos.PaginatedResponse;
import dtos.ProblemAddDTO;
import dtos.ProblemGetDTO;
import exceptions.conflict.ConflictException;
import exceptions.resource_not_found.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import models.Player;
import models.Problem;
import repositories.ProblemRepository;
import utils.LightsOutSolver;

import java.util.List;

import static utils.QueryArgs.checkAllArgs;

@ApplicationScoped
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final PlayerService playerService;
    public ProblemService(ProblemRepository problemRepository, PlayerService playerService) {
        this.problemRepository = problemRepository;
        this.playerService = playerService;
    }

    public PaginatedResponse<ProblemGetDTO> getAllProblems(int page, int size, String sortBy, String sortOrder) {
        String orderBy = switch (sortBy.toLowerCase()) {
            case "username" -> "p.creator.username " + sortOrder;
            case "description" -> "LENGTH(p.description) " + sortOrder;
            default -> throw new BadRequestException("Invalid sort field: " + sortBy);
        };

        checkAllArgs(page, size, sortOrder);

        List<ProblemGetDTO> problems = problemRepository
                .find("from Problem p order by " + orderBy)
                .page(page, size)
                .list()
                .stream()
                .map(problem -> new ProblemGetDTO(problem.getDescription(), problem.getCreator().getUsername()))
                .toList();

        long totalElements = problemRepository.count();
        return PaginatedResponse.createPaginatedResponse(problems, page, size, totalElements);
    }

    public PaginatedResponse<ProblemGetDTO> getProblemsByCreator(String username, int page, int size, String sortOrder) {
        checkAllArgs(page, size, sortOrder);

        playerService.checkIfPlayerExists(username);

        List<ProblemGetDTO> problems = problemRepository
                .find("from Problem p where p.creator.username = ?1 order by LENGTH(p.description) " + sortOrder, username)
                .page(page, size)
                .list()
                .stream()
                .map(problem -> new ProblemGetDTO(problem.getDescription(), problem.getCreator().getUsername()))
                .toList();

        long totalElements = problemRepository.count("creator.username", username);
        return PaginatedResponse.createPaginatedResponse(problems, page, size, totalElements);
    }

    public ProblemGetDTO getProblemById(Long id) {
        Problem problem = getProblem(id);
        return new ProblemGetDTO(problem.getDescription(), problem.getCreator().getUsername());
    }

    @Transactional
    public ProblemGetDTO createProblem(ProblemAddDTO problemAddDTO) {
        if (problemRepository.find("description", problemAddDTO.description()).firstResult() != null)
            throw new ConflictException("Problem already exists");

        if (!problemAddDTO.description().matches("[01]+"))
            throw new BadRequestException("String must contain only 0 and 1 characters");

        Player creator = playerService.getPlayer(problemAddDTO.creator());

        LightsOutSolver.checkIfSolvable(problemAddDTO.description());

        Problem problem = new Problem();
        problem.setDescription(problemAddDTO.description());
        problem.setCreator(creator);
        problemRepository.persist(problem);

        return new ProblemGetDTO(problem.getDescription(), problemAddDTO.creator());
    }

    public void checkIfProblemExists(Long id) {
        if (problemRepository.count("id", id) == 0) {
            throw new NotFoundException("Problem with id " + id + " not found");
        }
    }

    public Problem getProblem(Long id) {
        Problem problem = problemRepository.findById(id);
        if (problem == null)
            throw new ResourceNotFoundException("Problem with id " + id + " not found");

        return problem;
    }
}
