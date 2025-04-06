package controllers;

import dtos.PaginatedResponse;
import dtos.ProblemAddDTO;
import dtos.ProblemGetDTO;
import exceptions.ErrorResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.ResponseStatus;
import services.ProblemService;

@Tag(name = "Problem Controller", description = "Problems are represented as a flattened matrix in a string")
@Path("/problems")
public class ProblemController {
    private final ProblemService problemService;
    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GET
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
                                  "description": "011101110",
                                  "solver": "player1"
                                },
                                {
                                  "description": "011101000",
                                  "solver": "player8"
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
    public PaginatedResponse<ProblemGetDTO> getAllProblems(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sortBy") @DefaultValue("username") String sortBy,
            @QueryParam("sortOrder") @DefaultValue("asc") String sortOrder
    ) {
        return problemService.getAllProblems(page, size, sortBy, sortOrder);
    }

    @GET
    @Path("/creator/{username}")
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
                      "description": "011101110",
                      "solver": "player1"
                    },
                    {
                      "description": "011101000",
                      "solver": "player1"
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
                @ExampleObject(name = "Player Not Found",
                        summary = "Player Not Found",
                        value = "{ \"message\": \"Player player17 not found\" }")
            }
        ))
    public PaginatedResponse<ProblemGetDTO> getProblemsByCreator(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sortOrder") @DefaultValue("asc") String sortOrder,
            @PathParam("username") String username
    ) {
        return problemService.getProblemsByCreator(username, page, size, sortOrder);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "OK",
        content = @Content(
            schema = @Schema(implementation = ProblemGetDTO.class),
            examples = {
                @ExampleObject(
                        name = "Valid Response",
                        summary = "Valid Response",
                        value = """
                                {
                                  "description": "011101110",
                                  "solver": "player1"
                                }
                                """
                )
            }
        ))
    @APIResponse(responseCode = "404", description = "Not Found",
        content = @Content(
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                @ExampleObject(name = "Problem Not Found",
                        summary = "Problem not found",
                        value = "{ \"message\": \"Problem with id 1234 not found\" }")
            }
        ))
    public ProblemGetDTO getProblemById(@PathParam("id") Long id) {
        return problemService.getProblemById(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(201)
    @APIResponse(responseCode = "201", description = "OK",
        content = @Content(
            schema = @Schema(implementation = ProblemGetDTO.class),
            examples = {
                @ExampleObject(
                    name = "Valid Response",
                    summary = "Valid Response",
                    value = """
                            {
                              "description": "011101110",
                              "solver": "player1"
                            }
                            """
                )
            }
        ))
    @APIResponse(responseCode = "400", description = "Bad Request",
        content = @Content(
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                @ExampleObject(name = "Not Solvable",
                        summary = "Not Solvable",
                        value = "{ \"message\": \"Problem not solvable\" }"),
                @ExampleObject(name = "Not right structure",
                        summary = "Not right structure",
                        value = "{ \"message\": \"Problem must be a square\" }"),
                @ExampleObject(name = "Wrong size",
                        summary = "Wrong size",
                        value = "{ \"message\": \"Problem must be off size between 3x3 and 8x8\" }"),
                @ExampleObject(name = "Wrong char",
                        summary = "Wrong char",
                        value = "{ \"message\": \"Problem description can contain only 0 and 1 characters\" }")
            }
        ))
    @APIResponse(responseCode = "404", description = "Not Found",
        content = @Content(
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                @ExampleObject(name = "Player Not Found",
                        summary = "Player Not Found",
                        value = "{ \"message\": \"Player player17 not found\" }")
            }
        ))
    @APIResponse(responseCode = "409", description = "Conflict",
        content = @Content(
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                @ExampleObject(name = "Problem Already Exists",
                        summary = "Problem Already Exists",
                        value = "{ \"message\": \"Problem already exists\" }")
            }
        ))
    @RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProblemAddDTO.class),
                    examples = @ExampleObject(
                            name = "Valid Request Body",
                            summary = "Valid Request Body",
                            value = "{\"description\": \"101110111\", \"creator\": \"player19\"}")
            ))
    public ProblemGetDTO createProblem(@Valid ProblemAddDTO problemAddDTO) {
        return problemService.createProblem(problemAddDTO);
    }
}
