package com.kentilini.server.service;

import com.kentilini.server.entity.Account;
import com.kentilini.server.entity.EntityManagerService;
import com.kentilini.server.entity.User;
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

    public Account createNewAccountForUser(BigInteger userId){
        User user = userService.getUserById(userId);
        Account newAcc = new Account();
        newAcc.setBalance(0d);
        newAcc.setStatus(Account.AccountStatus.OPEN);
        newAcc.setOwner(user);
        return saveOrUpdate(newAcc);
    }


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

    public void transferMoney(Long fromId, Long toId, Double amount) {
        Account from = getAccountById(fromId);
        Account to = getAccountById(toId);
        transferMoney(from, to, amount);
    }

    public void transferMoney(final Account from, final Account to, Double amount) {
        AccountPaymentsChecker object = new AccountPaymentsChecker(from, to, amount).verifyAccountsStatus().verifyEnoughMoney().verifyPositiveBalance().verifySameAccount();

        EntityManager em = entityManagerService.createEntityManager();
        lockManager.executeLocked(object, () -> {
            //DoubleCheck
            Account fromA = getAccountById(from.getId());
            Account toA = getAccountById(to.getId());
            new AccountPaymentsChecker(fromA, toA, amount).verifyAccountsStatus().verifyEnoughMoney().verifyPositiveBalance().verifySameAccount();

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
