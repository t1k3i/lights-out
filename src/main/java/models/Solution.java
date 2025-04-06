package models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Solution {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Player solver;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Problem problem;

    @OneToMany(mappedBy = "solution", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<SolutionStep> steps;

    public Long getId() {
        return id;
    }

    public Player getSolver() {
        return solver;
    }

    public Problem getProblem() {
        return problem;
    }

    public List<SolutionStep> getSteps() {
        return steps;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSolver(Player solver) {
        this.solver = solver;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public void setSteps(List<SolutionStep> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "id=" + id +
                ", solver=" + solver +
                ", problem=" + problem +
                ", steps=" + steps +
                '}';
    }
}
