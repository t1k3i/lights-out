package repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import models.Player;

@ApplicationScoped
public class PlayerRepository implements PanacheRepository<Player> {
}
