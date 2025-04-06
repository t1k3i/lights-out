package dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProblemAddDTO(
        @NotBlank(message = "Description must not be empty")
        @Size(min = 9, max = 564, message = "Problem must be off size between 3x3 and 8x8")
        String description,
        String creator
) {}
