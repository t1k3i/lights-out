package repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import models.Problem;

@ApplicationScoped
public class ProblemRepository implements PanacheRepository<Problem> {
}
