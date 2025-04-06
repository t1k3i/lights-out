package dtos;

import jakarta.validation.constraints.*;

public record PlayerAddDTO(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,
        @Min(value = 1, message = "Age must be at least 1")
        @Max(value = 140, message = "Nobody is older than 140")
        @NotNull(message = "Age cannot be null")
        Integer age
) {}
