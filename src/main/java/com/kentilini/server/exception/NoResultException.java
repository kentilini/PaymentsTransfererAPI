package com.kentilini.server.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NoResultException extends WebApplicationException{
    public NoResultException(String message) {
        super(Response.status(Response.Status.NOT_FOUND)
                .entity(message).type(MediaType.TEXT_PLAIN).build());
    }
}