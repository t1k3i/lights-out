package dtos;

import java.util.List;

public record SolutionGetDTO(String solver, String problem, List<SolutionStepGetDTO> steps) {
}
