package services;

import dtos.PaginatedResponse;
import dtos.PlayerAddDTO;
import dtos.PlayerGetDTO;
import exceptions.conflict.ConflictException;
import exceptions.resource_not_found.ResourceNotFoundException;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import models.Player;
import repositories.PlayerRepository;

import java.util.List;

import static utils.QueryArgs.checkAllArgs;

@ApplicationScoped
public class PlayerService {
    private final PlayerRepository playerRepository;
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public PaginatedResponse<PlayerGetDTO> getAllPlayers(int page, int size, String sortBy, String sortOrder) {
        if (!List.of("username", "age").contains(sortBy))
            throw new BadRequestException("Invalid sort field: " + sortBy);

        checkAllArgs(page, size, sortOrder);

        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.Descending : Sort.Direction.Ascending;

        List<PlayerGetDTO> players = playerRepository.findAll(Sort.by(sortBy, direction))
                .page(Page.of(page, size))
                .list()
                .stream()
                .map(player -> new PlayerGetDTO(player.getUsername(), player.getAge()))
                .toList();

        long totalElements = playerRepository.count();
        return PaginatedResponse.createPaginatedResponse(players, page, size, totalElements);
    }

    public PlayerGetDTO getPlayerByUsername(String username) {
        Player player = getPlayer(username);
        return new PlayerGetDTO(player.getUsername(), player.getAge());
    }

    @Transactional
    public PlayerGetDTO createPlayer(PlayerAddDTO playerDTO) {
        if (getPlayer(playerDTO.username()) != null)
            throw new ConflictException("Player " + playerDTO.username() + " already exists");

        Player player = new Player();
        player.setUsername(playerDTO.username());
        player.setAge(playerDTO.age());
        playerRepository.persist(player);

        return new PlayerGetDTO(player.getUsername(), player.getAge());
    }

    public Player getPlayer(String username) {
        Player player = playerRepository.find("username", username).firstResult();
        if (player == null)
            throw new ResourceNotFoundException("Player " + username + " not found");

        return player;
    }

    public void checkIfPlayerExists(String username) {
        if (playerRepository.count("username", username) == 0) {
            throw new ResourceNotFoundException("Player " + username + " not found");
        }
    }
}
