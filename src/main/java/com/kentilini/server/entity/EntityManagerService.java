package com.kentilini.server.entity;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Service for creating entity managers associated with database, configured via properties.
 */
@Singleton
public class EntityManagerService {
    //ToDo: implement multiple DB units
    String persistenceUnitName = "LocalDB";

    private EntityManagerFactory entityManagerFactory;

    @PostConstruct
    private void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    //ToDo: implement closeable API
    public EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}