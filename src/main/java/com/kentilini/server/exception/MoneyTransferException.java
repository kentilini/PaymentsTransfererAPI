package com.kentilini.server.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MoneyTransferException extends WebApplicationException {
    public MoneyTransferException(String message) {
        super(Response.status(Response.Status.PRECONDITION_FAILED)
                .entity(message).type(MediaType.TEXT_PLAIN).build());
    }
}



