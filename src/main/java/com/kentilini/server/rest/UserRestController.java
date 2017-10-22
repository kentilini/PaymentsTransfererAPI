package com.kentilini.server.rest;

import com.kentilini.server.entity.User;
import com.kentilini.server.exception.NoResultException;
import com.kentilini.server.exception.NonUniqueResultException;
import com.kentilini.server.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigInteger;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestController {

    @Inject
    UserService userService;


    /**
     *
     * @summary Returns the array of user Ids.
     * @param maxCount max number of result should be returned
     * @param pageNumber number of page from which result should be returned
     * @return List of User Ids
     */
    @GET
    @Path("getAllIds/{count}/{page}")
    public List<BigInteger> getAllUserIds(@PathParam("count") int maxCount, @PathParam("page") int pageNumber) {
        return userService.getAllUsersIds(maxCount, pageNumber);
    }

    /**
     *
     * @summary Finds user account stored at DB by Id
     * @param id id of user account to search
     * @return found User value
     * @throws NonUniqueResultException if search results will be more than 1
     * @throws NoResultException if user is not stored at DB
     */
    @GET
    @Path("{id}")
    public User getUserById(@PathParam("id") BigInteger id) {
        return userService.getUserById(id);
    }

    /**
     *
     * @summary Create or Updates User Entity value and stores it into DB. This method is transactional.
     * @param user new value of user that will be updated or added into DB
     * @return updated\added user value
     */
    @PUT
    @Path("addOrUpdateUser")
    public User addOrUpdateUser(User user) {
        return userService.saveOrUpdate(user);
    }

}
