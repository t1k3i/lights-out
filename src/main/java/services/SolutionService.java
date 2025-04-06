package services;

import dtos.*;
import exceptions.wrong_solution.WrongSolutionException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import models.Player;
import models.Problem;
import models.Solution;
import models.SolutionStep;
import repositories.SolutionRepository;
import utils.LightsOutSolver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static utils.QueryArgs.checkAllArgs;

@ApplicationScoped
public class SolutionService {
    private final ProblemService problemService;
    private final SolutionRepository solutionRepository;
    private final PlayerService playerService;

    public SolutionService(ProblemService problemService, SolutionRepository solutionRepository,
                           PlayerService playerService) {
        this.problemService = problemService;
        this.solutionRepository = solutionRepository;
        this.playerService = playerService;
    }

    public PaginatedResponse<SolutionGetDTO> getAllSolutions(int page, int size, String sortBy, String sortOrder) {
        String orderBy = switch (sortBy.toLowerCase()) {
            case "solver" -> "s.solver.username " + sortOrder;
            case "steps" -> "size(s.steps) " + sortOrder;
            case "problem" -> "length(s.problem.description) " + sortOrder;
            default ->
                    throw new BadRequestException("Invalid sort field: " + sortBy);
        };

        checkAllArgs(page, size, sortOrder);

        List<Solution> solutions = solutionRepository
                .find("SELECT s FROM Solution s ORDER BY " + orderBy)
                .page(page, size)
                .list();

        long totalElements = solutionRepository.count();
        return PaginatedResponse.createPaginatedResponse(toSolutionGetDTO(solutions), page, size, totalElements);
    }

    public PaginatedResponse<SolutionGetDTO> getSolutionsBySolver(String username, int page, int size, String sortBy, String sortOrder) {
        String orderBy = switch (sortBy.toLowerCase()) {
            case "steps" -> "size(s.steps) " + sortOrder;
            case "problem" -> "length(s.problem.description) " + sortOrder;
            default -> throw new BadRequestException("Invalid sort field: " + sortBy);
        };

        checkAllArgs(page, size, sortOrder);

        playerService.checkIfPlayerExists(username);

        List<Solution> solutions = solutionRepository
                .find("SELECT s FROM Solution s WHERE s.solver.username = ?1 ORDER BY " + orderBy, username)
                .page(page, size)
                .list();

        long totalElements = solutionRepository.count("solver.username", username);
        return PaginatedResponse.createPaginatedResponse(toSolutionGetDTO(solutions), page, size, totalElements);
    }

    public PaginatedResponse<SolutionGetDTO> getSolutionsByProblemId(Long id, int page, int size, String sortBy, String sortOrder) {
        String orderBy = switch (sortBy.toLowerCase()) {
            case "steps" -> "size(s.steps) " + sortOrder;
            case "solver" -> "s.solver.username " + sortOrder;
            default -> throw new BadRequestException("Invalid sort field: " + sortBy);
        };

        checkAllArgs(page, size, sortOrder);

        problemService.checkIfProblemExists(id);

        List<Solution> solutions = solutionRepository
                .find("SELECT s FROM Solution s WHERE s.problem.id = ?1 ORDER BY " + orderBy, id)
                .page(page, size)
                .list();

        long totalElements = solutionRepository.count("problem.id", id);
        return PaginatedResponse.createPaginatedResponse(toSolutionGetDTO(solutions), page, size, totalElements);
    }

    @Transactional
    public SolutionGetDTO createSolution(SolutionAddDTO solutionAddDTO) {
        Player solver = playerService.getPlayer(solutionAddDTO.solver());

        Problem problem = problemService.getProblem(solutionAddDTO.problemId());

        checkSolutionSteps(solutionAddDTO.steps(), problem.getDescription().length());

        if (!LightsOutSolver.checkIfCorrectSolution(problem.getDescription(), solutionAddDTO.steps()))
            throw new WrongSolutionException("Not correct solution");

        Solution solution = new Solution();
        solution.setSolver(solver);
        solution.setProblem(problem);
        solution.setSteps(new ArrayList<>());

        for (SolutionStepAddDTO stepDTO : solutionAddDTO.steps()) {
            SolutionStep step = new SolutionStep();
            step.setMove(stepDTO.move(solution.getProblem().getDescription().length()));
            step.setStepIndex(stepDTO.stepIndex());
            step.setSolution(solution);
            solution.getSteps().add(step);
        }
        solutionRepository.persist(solution);

        List<SolutionStepGetDTO> stepDTOs = solution.getSteps().stream()
                .map(SolutionStepGetDTO::convertToDTO)
                .toList();

        return new SolutionGetDTO(solutionAddDTO.solver(), problem.getDescription(), stepDTOs);
    }

    private List<SolutionGetDTO> toSolutionGetDTO(List<Solution> solutions) {
        return solutions.stream()
                .map(solution -> {
                    List<SolutionStepGetDTO> solutionStepGetDTOS = solution.getSteps().stream()
                            .map(SolutionStepGetDTO::convertToDTO)
                            .toList();

                    return new SolutionGetDTO(
                            solution.getSolver().getUsername(),
                            solution.getProblem().getDescription(),
                            solutionStepGetDTOS
                    );
                })
                .toList();
    }

    private void checkSolutionSteps(List<SolutionStepAddDTO> solutionStepAddDTOS, int problemLength) {
        int n = (int) Math.sqrt(problemLength);

        solutionStepAddDTOS.sort(Comparator.comparingInt(SolutionStepAddDTO::stepIndex));

        int expectedIndex = 1;
        for (SolutionStepAddDTO step : solutionStepAddDTOS) {
            int x = step.x();
            int y = step.y();
            int stepIndex = step.stepIndex();

            if (stepIndex != expectedIndex)
                throw new BadRequestException("Invalid step index " + stepIndex);

            if (x < 0 || x >= n || y < 0 || y >= n)
                throw new BadRequestException("Step " + stepIndex + " is out of bounds");

            expectedIndex++;
        }
    }
}
