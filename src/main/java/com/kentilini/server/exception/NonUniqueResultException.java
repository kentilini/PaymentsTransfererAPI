package com.kentilini.server.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NonUniqueResultException extends WebApplicationException {
    public NonUniqueResultException(String message) {
        super(Response.status(Response.Status.CONFLICT)
                .entity(message).type(MediaType.TEXT_PLAIN).build());
    }
}
