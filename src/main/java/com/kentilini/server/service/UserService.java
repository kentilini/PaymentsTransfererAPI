package com.kentilini.server.service;

import com.kentilini.server.entity.EntityManagerService;
import com.kentilini.server.entity.User;
import com.kentilini.server.utils.VerifyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;

@Singleton
public class UserService {
    //ToDo: provide user fields verification
    private final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Inject
    private EntityManagerService entityManagerService;

    public User getUserById(BigInteger userId) {
        EntityManager em = entityManagerService.createEntityManager();
        LOG.info("Try to find users by id: " + userId);
        try {
            TypedQuery<User> query = em.createNamedQuery("User.getById", User.class);
            query.setParameter("id", userId);
            List<User> resultList = query.getResultList();
            LOG.info("Search result size: " + resultList.size());
            VerifyResult.isOneResult(resultList);

            return resultList.get(0);
        } finally {
            em.close();
        }
    }

    public List<BigInteger> getAllUsersIds(int maxCount, int pageNumber) {
        EntityManager em = entityManagerService.createEntityManager();
        try {
            TypedQuery<BigInteger> query = em.createNamedQuery("User.getAllIds", BigInteger.class);
            query.setFirstResult(maxCount * pageNumber);
            query.setMaxResults(maxCount);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public User saveOrUpdate(User user) {
        EntityManager em = entityManagerService.createEntityManager();
        try {
            em.getTransaction().begin();
            if (user.getId() == null) {
                em.persist(user);
            } else {
                em.merge(user);
            }
            em.getTransaction().commit();
            return user;
        } finally {
            em.close();
        }
    }

}
