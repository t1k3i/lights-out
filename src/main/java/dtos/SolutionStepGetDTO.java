package dtos;

import models.Problem;
import models.Solution;
import models.SolutionStep;

public record SolutionStepGetDTO(Integer x, Integer y, Integer stepIndex) {

    public static SolutionStepGetDTO convertToDTO(SolutionStep solutionStep) {
        Solution solution = solutionStep.getSolution();
        Problem problem = solution.getProblem();
        int length = problem.getDescription().length();
        int n = (int) Math.sqrt(length);
        int x = solutionStep.getMove() % n;
        int y = (n - 1) - solutionStep.getMove() / n;
        return new SolutionStepGetDTO(x, y, solutionStep.getStepIndex());
    }

}
