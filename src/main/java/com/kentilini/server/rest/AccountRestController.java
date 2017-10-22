package com.kentilini.server.rest;

import com.kentilini.server.entity.Account;
import com.kentilini.server.entity.User;
import com.kentilini.server.exception.MoneyTransferException;
import com.kentilini.server.exception.NoResultException;
import com.kentilini.server.exception.NonUniqueResultException;
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


    /**
     *
     * @summary This method gets in RequestBody new Account object, and updates stored at DB initial value.
     * @param account the new value, that should be setted
     * @return updated account value
     * @throws todo: should throw exceptions on parameters verification
     */
    @PUT
    @Path("addOrUpdateUserAccount")
    public Account addOrUpdateAccount(Account account) {
        return accountService.saveOrUpdate(account);
    }


    /**
     *
     * @summary Stores in DB new user payments account
     * @param userId user primary key. By this key the payments account will be added
     * @return created account value
     */
    @PUT
    @Path("addUserAccount/{userId}")
    public Account addUserAccount(@PathParam("userId") BigInteger userId) {
        return accountService.createNewAccountForUser(userId);
    }


    /**
     *
     * @summary blocks payments account
     * @param id id of account that should be blocked
     * @return updated account value
     */
    @PUT
    @Path("block/{id}")
    public Account blockAccount(@PathParam("id") Long id){
        return accountService.changeAccountStatus(id, Account.AccountStatus.BLOCKED);
    }

    /**
     *
     * @summary archive payments account
     * @param id id of account that should be archived
     * @return updated account value
     */
    @PUT
    @Path("archive/{id}")
    public Account archiveAccount(@PathParam("id") Long id){
        return accountService.changeAccountStatus(id, Account.AccountStatus.ARCHIVED);
    }

    /**
     *
     * @summary open closed or archived payments account
     * @param id id of account that should be opened
     * @return updated account value
     */
    @PUT
    @Path("renew/{id}")
    public Account renewAccount(@PathParam("id") Long id){
        return accountService.changeAccountStatus(id, Account.AccountStatus.OPEN);
    }

    /**
     *
     * @summary This method represents payment transfer form the external(unknown) checked transaction.
     * There for we could only add money to this account.
     * @param id primary key of the Account.
     * @param amount the size of amount, that should be added to account balance
     * @return updated account value
     */
    @PUT
    @Path("replenish/{accountId}/{amount}")
    public Account replenishAccount(@PathParam("accountId") Long id, @PathParam("amount") Double amount) {
        return accountService.replenish(id, amount);
    }

    /**
     *
     * @summary Shows all created payments accounts for registered user
     * @param user needed to get accounts list of this user
     * @return List of registered on user payment accounts
     */
    @POST
    public List<Account> getUserAccounts(User user) {
        return accountService.getUserAccounts(user);
    }


    /**
     *
     * @summary Provides a payment transaction between 2 accounts.
     *
     * @param fromId id of the payment accounts who pays
     * @param toId id of the payment account where should be transferred money
     * @param amount the amount\sum of transaction
     * @return Empty ok body, if transaction executed correctly.
     * @throws MoneyTransferException if at least one of accounts doesn't support money transferring. i.e. in BLOCKED status
     * @throws MoneyTransferException if account balance contains not enough money for execution of transaction
     * @throws MoneyTransferException if tou trying to transfer money form/to the same account
     * @throws MoneyTransferException if the amount price have zero or negative value
     * @throws MoneyTransferException if the from account doesn't have positive balance
     * @throws NonUniqueResultException if found several account results by fromId or toId
     * @throws NoResultException if fromId or toId account doesn't exist
     */
    @POST
    @Path("provideTransaction")
    public Response provideTransaction(@QueryParam("from") Long fromId,@QueryParam("to") Long toId,@QueryParam("amount") Double amount){
        accountService.transferMoney(fromId, toId, amount);
        return Response.ok().build();
    }

}
