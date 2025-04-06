package exceptions.wrong_solution;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class WrongSolutionException extends WebApplicationException {
    public WrongSolutionException(String message) {
        super(message, Response.Status.BAD_REQUEST);
    }
}
