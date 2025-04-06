package exceptions;

import exceptions.conflict.ConflictException;
import exceptions.not_solvable.NotSolvableException;
import exceptions.resource_not_found.ResourceNotFoundException;
import exceptions.wrong_solution.WrongSolutionException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        String message = "Internal Server Error";

        if (exception instanceof ResourceNotFoundException ||
                exception instanceof NotFoundException) {
            status = Response.Status.NOT_FOUND;
            message = exception.getMessage();
        } else if (exception instanceof ConflictException) {
            status = Response.Status.CONFLICT;
            message = exception.getMessage();
        } else if (exception instanceof BadRequestException ||
                    exception instanceof NotSolvableException ||
                    exception instanceof WrongSolutionException) {
            status = Response.Status.BAD_REQUEST;
            message = exception.getMessage();
        }

        ErrorResponse errorResponse = new ErrorResponse(message);

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorResponse)
                .build();
    }
}
