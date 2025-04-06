package dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SolutionAddDTO(
        @NotNull(message = "Solver can not be null")
        String solver,
        @NotNull(message = "Id can not be null")
        Long problemId,
        @NotNull(message = "Steps can not be null or empty")
        @NotEmpty(message = "Steps can not be empty or empty")
        List<SolutionStepAddDTO> steps
)
{ }
