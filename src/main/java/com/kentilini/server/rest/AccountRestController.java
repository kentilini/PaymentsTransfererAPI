package com.kentilini.server.rest;

import com.kentilini.server.entity.Account;
import com.kentilini.server.entity.User;
import com.kentilini.server.service.AccountService;
import com.kentilini.server.service.UserService;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountRestController {

    @Inject
    AccountService accountService;

    @PUT
    @Path("addOrUpdateUserAccount")
    public Account addOrUpdateAccount(Account account) {
        return accountService.saveOrUpdate(account);
    }

    @PUT
    @Path("addUserAccount/{userId}")
    public Account addUserAccount(@PathParam("userId") BigInteger userId) {
        return accountService.createNewAccountForUser(userId);
    }

    @PUT
    @Path("replenish/{accountId}/{amount}")
    public Account replenishAccount(@PathParam("accountId") Long id, @PathParam("amount") Double amount) {
        return accountService.replenish(id, amount);
    }

    @POST
    public List<Account> getUserAccounts(User user) {
        return accountService.getUserAccounts(user);
    }

    @POST
    @Path("provideTransaction")
    public Response provideTransaction(@QueryParam("from") Long fromId,@QueryParam("to") Long toId,@QueryParam("amount") Double amount){
        accountService.transferMoney(fromId, toId, amount);
        return Response.ok().build();
    }

}
