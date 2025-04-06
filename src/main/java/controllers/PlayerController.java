package controllers;

import dtos.PaginatedResponse;
import dtos.PlayerAddDTO;
import dtos.PlayerGetDTO;
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
import services.PlayerService;

@Path("/players")
public class PlayerController {
    private final PlayerService playerService;
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "OK",
        content = @Content(
            schema = @Schema(implementation = PaginatedResponse.class),
            examples = {
                @ExampleObject(name = "Valid Response",
                        summary = "Valid Response",
                        value = """
                                {
                                  "content": [
                                    {
                                      "username": "player1",
                                      "age": 30
                                    },
                                    {
                                      "username": "player2",
                                      "age": 25
                                    }
                                  ],
                                  "totalElements": 2,
                                  "totalPages": 1,
                                  "currentPage": 0,
                                  "size": 10,
                                  "hasNext": false
                                }
                                """)
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
    public PaginatedResponse<PlayerGetDTO> getAllPlayers(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sortBy") @DefaultValue("username") String sortBy,
            @QueryParam("sortOrder") @DefaultValue("asc") String sortOrder
    ) {
        return playerService.getAllPlayers(page, size, sortBy, sortOrder);
    }

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "OK",
        content = @Content(
            schema = @Schema(implementation = PlayerGetDTO.class),
            examples = {
                @ExampleObject(name = "Valid Response",
                        summary = "Valid Response",
                        value = "{ \"username\": \"player1\", \"age\": 25 }")
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
    public PlayerGetDTO getPlayerByUsername(@PathParam("username") String username) {
        return playerService.getPlayerByUsername(username);
    }

    @POST
    @ResponseStatus(201)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "201", description = "CREATED",
        content = @Content(
            schema = @Schema(implementation = PlayerGetDTO.class),
            examples = {
                @ExampleObject(name = "Valid Response",
                        summary = "Valid Response",
                        value = "{ \"username\": \"player1\", \"age\": 25 }")
            }
        ))
    @APIResponse(responseCode = "400", description = "Bad Request",
        content = @Content(
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                @ExampleObject(
                        name = "Invalid Username",
                        summary = "Invalid Username",
                        value = "{ \"message\": \"Username must be between 3 and 50 characters\" }"
                ),
                @ExampleObject(
                        name = "Username Can Not Be Blank",
                        summary = "Username Can Not Be Blank",
                        value = "{ \"message\": \"Username cannot be blank\" }"
                ),
                @ExampleObject(
                        name = "Invalid Age 1",
                        summary = "Invalid Age 1",
                        value = "{ \"message\": \"Age must be at least 1\" }"
                ),
                @ExampleObject(
                        name = "Invalid Age 2",
                        summary = "Invalid Age 2",
                        value = "{ \"message\": \"Nobody is older than 140\" }"
                ),
                @ExampleObject(
                        name = "Age Cannot Be Null",
                        summary = "Age Cannot Be Null",
                        value = "{ \"message\": \"Age cannot be null\" }"
                ),
            }
        ))
    @APIResponse(responseCode = "409", description = "Conflict",
        content = @Content(
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                @ExampleObject(name = "Player Already Exists",
                        summary = "Player Already Exists",
                        value = "{ \"message\": \"Player player1 already exists\" }")
            }
        ))
    @RequestBody(
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = PlayerAddDTO.class),
            examples = @ExampleObject(
                    name = "Valid Request Body",
                    summary = "Valid Request Body",
                    value = "{\"username\": \"player7\", \"age\": 25}")
        ))
    public PlayerGetDTO createPlayer(@Valid PlayerAddDTO player) {
        return playerService.createPlayer(player);
    }
}
