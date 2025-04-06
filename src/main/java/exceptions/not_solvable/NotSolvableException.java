package exceptions.not_solvable;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class NotSolvableException extends WebApplicationException {
    public NotSolvableException(String message) {
        super(message, Response.Status.BAD_REQUEST);
    }
}
