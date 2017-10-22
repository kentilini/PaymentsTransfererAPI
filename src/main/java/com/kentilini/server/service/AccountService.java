package com.kentilini.server.service;

import com.kentilini.server.entity.Account;
import com.kentilini.server.entity.EntityManagerService;
import com.kentilini.server.entity.User;
import com.kentilini.server.exception.MoneyTransferException;
import com.kentilini.server.exception.NoResultException;
import com.kentilini.server.exception.NonUniqueResultException;
import com.kentilini.server.utils.VerifyResult;
import com.kentilini.server.verify.AccountPaymentsChecker;
import de.jkeylockmanager.manager.KeyLockManager;
import de.jkeylockmanager.manager.KeyLockManagers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;

@Singleton
public class AccountService {
    private final Logger LOG = LoggerFactory.getLogger(AccountService.class);
    private final KeyLockManager lockManager = KeyLockManagers.newLock();

    @Inject
    private EntityManagerService entityManagerService;

    @Inject
    private UserService userService;

    /**
     * This method finds all user payments accounts
     * @return List of user Accounts
     */
    public List<Account> getUserAccounts(User user) {
        LOG.info("Search for user accounts with id: " + user.getId());
        EntityManager em = entityManagerService.createEntityManager();
        try {
            TypedQuery<Account> query = em.createNamedQuery("Account.getUserAccounts", Account.class);
            query.setParameter("user", user);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * This method find user payment account by id
     * @param id of the Account
     * @return finded Account value
     * @throws NonUniqueResultException if search results will be more than 1
     * @throws NoResultException if account is not stored at DB
     */
    public Account getAccountById(Long id) {
        EntityManager em = entityManagerService.createEntityManager();
        try {
            TypedQuery<Account> query = em.createNamedQuery("Account.getAccountById", Account.class);
            query.setParameter("id", id);
            List<Account> result = query.getResultList();
            VerifyResult.isOneResult(result);

            return result.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Creates new account for user with empty balance and open status.
     * @param userId id of the user
     * @return created Account value
     * @throws NoResultException if can't find user
     * @throws NonUniqueResultException if number of found users greater than 1
     */
    public Account createNewAccountForUser(BigInteger userId){
        User user = userService.getUserById(userId);
        Account newAcc = new Account();
        newAcc.setBalance(0d);
        newAcc.setStatus(Account.AccountStatus.OPEN);
        newAcc.setOwner(user);
        return saveOrUpdate(newAcc);
    }

    /**
     * This method replenish account balance for amount size
     * @param id of the Account
     * @param amount size of amount that should be added to the account
     * @return finded Account value
     * @throws NoResultException if account is not stored at DB
     * @throws NonUniqueResultException if number of found accounts greater than 1
     * @throws MoneyTransferException if amount is non positive value
     */
    public Account replenish(Long id, Double amount){
        Account account = getAccountById(id);
        AccountPaymentsChecker object = new AccountPaymentsChecker(account, null, amount).verifyAmount();
        EntityManager em = entityManagerService.createEntityManager();
        lockManager.executeLocked(object, () -> {
            try {
                account.setBalance(account.getBalance() + amount);
                em.getTransaction().begin();
                    em.merge(account);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
        });
        return getAccountById(id);
    }

    /**
     *
     * @summary Create or Updates Account Entity value and stores it into DB. This method is transactional.
     * @param account new value of account that will be updated or added into DB
     * @return updated\added account value
     */
    public Account saveOrUpdate(Account account) {
        EntityManager em = entityManagerService.createEntityManager();
        AccountPaymentsChecker object  = new AccountPaymentsChecker(account, null, null);
        lockManager.executeLocked(object, () -> {
            try {
                em.getTransaction().begin();
                if (account.getId() == null) {
                    em.persist(account);
                } else {
                    em.merge(account);
                }
                em.getTransaction().commit();
            } finally {
                em.close();
            }
        });
        return account;
    }

    /**
     *
     * @summary This method allows you to block|unblock|open the account status
     * @param id of the account
     * @param status new account status
     * @return updated account
     * @throws NonUniqueResultException if number of found accounts greater than 1
     * @throws NoResultException if account is not stored at DB
     */
    public Account changeAccountStatus(Long id, Account.AccountStatus status){
        return changeAccountStatus(getAccountById(id), status);
    }

    /**
     *
     * @summary This method allows you to block|unblock|open the account status
     * @param id of the account
     * @param status new account status
     * @return updated account
     */
    public Account changeAccountStatus(Account account, Account.AccountStatus status){
        account.setStatus(status);
        return saveOrUpdate(account);
    }

    /**
     *
     * @summary This method transfer money between to accounts
     * @param fromId account id who gives amount
     * @param toId account id who takes amount
     * @param amount size of amount
     *
     * @throws MoneyTransferException if at least one of accounts doesn't support money transferring. i.e. in BLOCKED status
     * @throws MoneyTransferException if account balance contains not enough money for execution of transaction
     * @throws MoneyTransferException if tou trying to transfer money form/to the same account
     * @throws MoneyTransferException if the amount price have zero or negative value
     * @throws MoneyTransferException if the from account doesn't have positive balance
     * @throws NonUniqueResultException if number of found accounts greater than 1
     * @throws NoResultException if account is not stored at DB
     */
    public void transferMoney(Long fromId, Long toId, Double amount) {
        Account from = getAccountById(fromId);
        Account to = getAccountById(toId);
        transferMoney(from, to, amount);
    }


    /**
     *
     * @summary This method transfer money between to accounts
     * @param from account who gives amount
     * @param to account who takes amount
     * @param amount size of amount
     *
     * @throws MoneyTransferException if at least one of accounts doesn't support money transferring. i.e. in BLOCKED status
     * @throws MoneyTransferException if account balance contains not enough money for execution of transaction
     * @throws MoneyTransferException if tou trying to transfer money form/to the same account
     * @throws MoneyTransferException if the amount price have zero or negative value
     * @throws MoneyTransferException if the from account doesn't have positive balance
     * @throws NonUniqueResultException if number of found accounts greater than 1
     * @throws NoResultException if account is not stored at DB
     */
    public void transferMoney(final Account from, final Account to, Double amount) {
        AccountPaymentsChecker object = new AccountPaymentsChecker(from, to, amount).verifyAccountsStatus().verifyEnoughMoney()
                .verifyPositiveBalance().verifySameAccount().verifyAmount();

        EntityManager em = entityManagerService.createEntityManager();
        lockManager.executeLocked(object, () -> {
            //DoubleCheck
            Account fromA = getAccountById(from.getId());
            Account toA = getAccountById(to.getId());
            new AccountPaymentsChecker(fromA, toA, amount).verifyAccountsStatus().verifyEnoughMoney().verifyPositiveBalance()
                    .verifySameAccount().verifyAmount();

            fromA.setBalance(fromA.getBalance() - amount);
            toA.setBalance(toA.getBalance() + amount);
            try {
                em.getTransaction().begin();
                em.merge(fromA);
                em.merge(toA);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
        });

    }

}
