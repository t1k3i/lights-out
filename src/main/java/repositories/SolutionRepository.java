package repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import models.Solution;

@ApplicationScoped
public class SolutionRepository implements PanacheRepository<Solution> {
}
