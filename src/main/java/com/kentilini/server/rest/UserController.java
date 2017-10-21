package com.kentilini.server.rest;

import com.kentilini.server.entity.User;
import com.kentilini.server.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigInteger;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;

    @GET
    @Path("getAllIds/{count}/{page}")
    public List<BigInteger> getAllUserIds(@PathParam("count") int maxCount, @PathParam("page") int pageNumber) {
        return userService.getAllUsersIds(maxCount, pageNumber);
    }

    @GET
    @Path("{id}")
    public User getUserById(@PathParam("id") BigInteger id) {
        return userService.getUserById(id);
    }

    @PUT
    @Path("addUser")
    public User addUser(User user) {
        return userService.saveOrUpdate(user);
    }

}
