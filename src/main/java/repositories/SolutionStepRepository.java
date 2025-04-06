package repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import models.SolutionStep;

@ApplicationScoped
public class SolutionStepRepository implements PanacheRepository<SolutionStep> {
}
