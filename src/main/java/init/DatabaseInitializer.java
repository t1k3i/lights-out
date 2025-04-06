package init;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import models.Player;
import models.Problem;
import repositories.PlayerRepository;
import repositories.ProblemRepository;

@ApplicationScoped
public class DatabaseInitializer {
    private final PlayerRepository playerRepository;
    private final ProblemRepository problemRepository;

    public DatabaseInitializer(PlayerRepository playerRepository, ProblemRepository problemRepository) {
        this.playerRepository = playerRepository;
        this.problemRepository = problemRepository;
    }

    @Startup
    @Transactional
    public void init() {
        Player player1 = new Player();
        player1.setUsername("john_doe");
        player1.setAge(25);
        playerRepository.persist(player1);

        Player player2 = new Player();
        player2.setUsername("jane_smith");
        player2.setAge(30);
        playerRepository.persist(player2);

        Problem problem1 = new Problem();
        problem1.setDescription("111001011");
        problem1.setCreator(player1);
        problemRepository.persist(problem1);

        Problem problem2 = new Problem();
        problem2.setDescription("000000000");
        problem2.setCreator(player2);
        problemRepository.persist(problem2);
    }
}
