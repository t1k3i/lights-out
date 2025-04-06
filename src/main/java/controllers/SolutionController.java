package controllers;

import dtos.*;
import exceptions.ErrorResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.ResponseStatus;
import services.SolutionService;

@Path("/solutions")
public class SolutionController {
    private final SolutionService solutionService;
    public SolutionController(SolutionService solutionService) {
        this.solutionService = solutionService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "OK",
        content = @Content(
            schema = @Schema(implementation = PaginatedResponse.class),
            examples = {
                @ExampleObject(
                        name = "Valid response",
                        summary = "Valid response",
                        value = """
                                {
                                  "content": [
                                    {
                                      "solver": "player1",
                                      "problemId": 12345,
                                      "steps": [
                                        {"x": 1, "y": 0, "stepIndex": 1},
                                        {"x": 2, "y": 2, "stepIndex": 2}
                                      ]
                                    },
                                    {
                                      "solver": "player2",
                                      "problemId": 67890,
                                      "steps": [
                                        {"x": 1, "y": 0, "stepIndex": 1}
                                      ]
                                    }
                                  ],
                                  "totalElements": 2,
                                  "totalPages": 1,
                                  "currentPage": 0,
                                  "size": 10,
                                  "hasNext": false
                                }
                                """
                )
            }
        ))
    @APIResponse(responseCode = "400", description = "Bad Request",
        content = @Content(
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                @ExampleObject(name = "Invalid Sort Field",
                        summary = "Invalid Sort Field",
                        value = "{ \"message\": \"Invalid sort field: score\" }"),
                @ExampleObject(name = "Invalid Sort Order",
                        summary = "Invalid Sort Order",
                        value = "{ \"message\": \"Invalid sort order. Allowed values are 'asc' or 'desc'.\" }"),
                @ExampleObject(name = "Invalid Page Number",
                        summary = "Invalid Page Number",
                        value = "{ \"message\": \"Invalid page number: -1\" }"),
                @ExampleObject(name = "Invalid Size Number",
                        summary = "Invalid Size Number",
                        value = "{ \"message\": \"Invalid size number: 0\" }")
            }
        ))
    public PaginatedResponse<SolutionGetDTO> getAllSolutions(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sortBy") @DefaultValue("solver") String sortBy,
            @QueryParam("sortOrder") @DefaultValue("asc") String sortOrder
    ) {
        return solutionService.getAllSolutions(page, size, sortBy, sortOrder);
    }

    @GET
    @Path("/solver/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "OK",
        content = @Content(
            schema = @Schema(implementation = PaginatedResponse.class),
            examples = {
                @ExampleObject(
                        name = "Valid Response",
                        summary = "Valid Response",
                        value = """
                                {
                                  "content": [
                                    {
                                      "solver": "player1",
                                      "problemId": 12345,
                                      "steps": [
                                        {"x": 1, "y": 0, "stepIndex": 1},
                                        {"x": 2, "y": 2, "stepIndex": 2}
                                      ]
                                    }
                                  ],
                                  "totalElements": 1,
                                  "totalPages": 1,
                                  "currentPage": 0,
                                  "size": 10,
                                  "hasNext": false
                                }
                                """
                )
            }
        ))
    @APIResponse(responseCode = "400", description = "Bad Request",
        content = @Content(
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                @ExampleObject(name = "Invalid Sort Field",
                        summary = "Invalid Sort Field",
                        value = "{ \"message\": \"Invalid sort field: score\" }"),
                @ExampleObject(name = "Invalid Sort Order",
                        summary = "Invalid Sort Order",
                        value = "{ \"message\": \"Invalid sort order. Allowed values are 'asc' or 'desc'.\" }"),
                @ExampleObject(name = "Invalid Page Number",
                        summary = "Invalid Page Number",
                        value = "{ \"message\": \"Invalid page number: -1\" }"),
                @ExampleObject(name = "Invalid Size Number",
                        summary = "Invalid Size Number",
                        value = "{ \"message\": \"Invalid size number: 0\" }")
            }
        ))
    @APIResponse(responseCode = "404", description = "Not Found",
        content = @Content(
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                @ExampleObject(
                        name = "Player Not Found",
                        summary = "Player Not Found",
                        value = "{ \"message\": \"Player player17 not found\" }"
                )
            }
        ))
    public PaginatedResponse<SolutionGetDTO> getSolutionsBySolver(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sortBy") @DefaultValue("steps") String sortBy,
            @QueryParam("sortOrder") @DefaultValue("asc") String sortOrder,
            @PathParam("username") String username
    ) {
        return solutionService.getSolutionsBySolver(username, page, size, sortBy, sortOrder);
    }

    @GET
    @Path("/problem/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "OK",
        content = @Content(
            schema = @Schema(implementation = PaginatedResponse.class),
            examples = {
                @ExampleObject(
                        name = "Single Page Example",
                        summary = "Paginated response with one element",
                        value = """
                                {
                                  "content": [
                                    {
                                      "solver": "player1",
                                      "problemId": 12345,
                                      "steps": [
                                        {"x": 1, "y": 0, "stepIndex": 1},
                                        {"x": 2, "y": 2, "stepIndex": 2}
                                      ]
                                    }
                                  ],
                                  "totalElements": 1,
                                  "totalPages": 1,
                                  "currentPage": 0,
                                  "size": 10,
                                  "hasNext": false
                                }
                                """
                )
            }
        ))
    @APIResponse(responseCode = "400", description = "Bad Request",
        content = @Content(
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                @ExampleObject(name = "Invalid Sort Field",
                        summary = "Invalid Sort Field",
                        value = "{ \"message\": \"Invalid sort field: score\" }"),
                @ExampleObject(name = "Invalid Sort Order",
                        summary = "Invalid Sort Order",
                        value = "{ \"message\": \"Invalid sort order. Allowed values are 'asc' or 'desc'.\" }"),
                @ExampleObject(name = "Invalid Page Number",
                        summary = "Invalid Page Number",
                        value = "{ \"message\": \"Invalid page number: -1\" }"),
                @ExampleObject(name = "Invalid Size Number",
                        summary = "Invalid Size Number",
                        value = "{ \"message\": \"Invalid size number: 0\" }")
            }
        ))
    @APIResponse(responseCode = "404", description = "Not Found",
        content = @Content(
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                @ExampleObject(
                        name = "Problem Not Found",
                        summary = "Problem Not Found",
                        value = "{ \"message\": \"Problem not found\" }"
                )
            }
        ))
    public PaginatedResponse<SolutionGetDTO> getSolutionsByProblemId(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sortBy") @DefaultValue("solver") String sortBy,
            @QueryParam("sortOrder") @DefaultValue("asc") String sortOrder,
            @PathParam("id") Long id) {
        return solutionService.getSolutionsByProblemId(id, page, size, sortBy, sortOrder);
    }

    @POST
    @ResponseStatus(201)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "201", description = "CREATED",
        content = @Content(
            schema = @Schema(implementation = SolutionGetDTO.class),
            examples = {
                @ExampleObject(
                        name = "Valid Solution",
                        summary = "Example of a created solution",
                        value = """
                                {
                                  "solver": "player1",
                                  "problemId": 12345,
                                  "steps": [
                                    {"x": 1, "y": 0, "stepIndex": 1},
                                    {"x": 2, "y": 2, "stepIndex": 2}
                                  ]
                                }
                                """
                )
            }
        ))
    @APIResponse(responseCode = "400", description = "Bad Request",
        content = @Content(
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                @ExampleObject(name = "Wrong solution",
                        summary = "Wrong solution",
                        value = "{ \"message\": \"Not correct solution\" }"),
                @ExampleObject(name = "Incorrect solution step",
                        summary = "Incorrect solution step",
                        value = "{ \"message\": \"Step index, x and y can not be empty\" }"),
                @ExampleObject(name = "Invalid step index",
                        summary = "Invalid step index",
                        value = "{ \"message\": \"Invalid step index 17\" }"),
                @ExampleObject(name = "Out of bounds",
                        summary = "Out of bounds",
                        value = "{ \"message\": \"Step 7 is out of bounds\" }"),
                @ExampleObject(
                        name = "Invalid solver",
                        summary = "Invalid solver",
                        value = "{ \"message\": \"Solver can not be null\" }"
                ),
                @ExampleObject(
                        name = "Invalid problem id",
                        summary = "Invalid problem id",
                        value = "{ \"message\": \"Id can not be null\" }"
                ),
                @ExampleObject(
                        name = "Invalid steps",
                        summary = "Invalid steps",
                        value = "{ \"message\": \"Steps can not be null or empty\" }"
                )
            }
        ))
    @APIResponse(responseCode = "404", description = "Not Found",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                @ExampleObject(name = "Problem not found",
                        summary = "Problem not found",
                        value = "{ \"message\": \"Problem with id 1234 not found\" }"),
                @ExampleObject(name = "Player not found",
                        summary = "Player not found",
                        value = "{ \"message\": \"Player player7 not found\" }"),
            }
        ))
    @RequestBody(
        content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = SolutionAddDTO.class),
                examples = @ExampleObject(
                        name = "Valid Request Body",
                        summary = "Valid Request Body",
                        value = """
                                {
                                    "solver": "player7",
                                    "problemId": 1234,
                                    "steps": [
                                        {"x": 1, "y": 0, "stepIndex": 1},
                                        {"x": 2, "y": 2, "stepIndex": 2}
                                    ]
                                }
                                """)
        ))
    public SolutionGetDTO createSolution(@Valid SolutionAddDTO solutionAddDTO) {
        return solutionService.createSolution(solutionAddDTO);
    }
}
