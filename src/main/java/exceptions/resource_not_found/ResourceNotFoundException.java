package exceptions.resource_not_found;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class ResourceNotFoundException extends WebApplicationException {
    public ResourceNotFoundException(String message) {
        super(message, Response.Status.NOT_FOUND);
    }
}
