package models;

import jakarta.persistence.*;

@Entity
@Table(name = "solution_step")
public class SolutionStep {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    public Solution solution;

    @Column(nullable = false)
    public Integer move;

    @Column(name = "step_index",nullable = false)
    public Integer stepIndex;

    public Long getId() {
        return id;
    }

    public Solution getSolution() {
        return solution;
    }

    public int getMove() {
        return move;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public void setStepIndex(int stepOrderIndex) {
        this.stepIndex = stepOrderIndex;
    }

    @Override
    public String toString() {
        return "SolutionStep{" +
                "id=" + id +
                ", solution=" + solution +
                ", move=" + move +
                ", stepOrderIndex=" + stepIndex +
                '}';
    }
}
