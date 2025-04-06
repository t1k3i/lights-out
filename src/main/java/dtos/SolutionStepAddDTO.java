package dtos;

import jakarta.validation.constraints.NotNull;

public record SolutionStepAddDTO(
        @NotNull(message = "Step index, x and y can not be empty")
        Integer x,
        @NotNull(message = "Step index, x and y can not be empty")
        Integer y,
        @NotNull(message = "Step index, x and y can not be empty")
        Integer stepIndex
) {
    public Integer move(int lengthOfProblem) {
        int n = (int) Math.sqrt(lengthOfProblem);
        return (n - 1 - y) * n + x;
    }
}
