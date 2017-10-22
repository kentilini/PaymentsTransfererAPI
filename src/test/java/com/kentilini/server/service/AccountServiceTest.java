package com.kentilini.server.service;

import com.kentilini.server.entity.Account;
import com.kentilini.server.entity.EntityManagerService;
import com.kentilini.server.entity.User;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.Collections;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;
import static org.mockito.Matchers.any;

public class AccountServiceTest {
    @Mock
    TypedQuery<Account> tq;

    @Mock
    EntityManager em;

    @Mock
    EntityManagerService entityManagerService;

    @Mock
    User user;

    @Mock
    UserService userService;

    @Mock
    Account account;

    @InjectMocks
    AccountService accountService;

    @BeforeClass
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void initMethod(){
        when(entityManagerService.createEntityManager()).thenReturn(em);
        when(tq.setParameter(any(String.class), any(String.class))).thenReturn(tq);

        doNothing().when(em).close();
        when(em.createNamedQuery(any(String.class), any(Class.class))).thenReturn(tq);
    }

    @AfterMethod
    private void tearDown(){

        reset(entityManagerService, userService, em, tq);
    }


    @Test
    public void testGetUserAccounts() throws Exception {
        when(tq.getResultList()).thenReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), accountService.getUserAccounts(user));

        verify(entityManagerService, times(1)).createEntityManager();
        verify(em, times(1)).createNamedQuery(eq("Account.getUserAccounts"), any(Class.class));
        verify(tq, times(1)).setParameter(any(String.class), any(String.class));
        verify(em, times(1)).close();
    }


    @Test
    public void testGetAccountById_success() throws Exception {
        when(tq.getResultList()).thenReturn(Collections.singletonList(account));

        assertEquals(account, accountService.getAccountById(1L));

        verify(entityManagerService, times(1)).createEntityManager();
        verify(em, times(1)).createNamedQuery(eq("Account.getAccountById"), any(Class.class));
        verify(tq, times(1)).setParameter(any(String.class), any(String.class));
        verify(em, times(1)).close();
    }

    @Test
    public void testCreateNewAccountForUser() throws Exception {
    }

    @Test
    public void testReplenish() throws Exception {
    }

    @Test
    public void testSaveOrUpdate() throws Exception {
    }

    @Test
    public void testChangeAccountStatus() throws Exception {
    }

    @Test
    public void testChangeAccountStatus1() throws Exception {
    }

    @Test
    public void testTransferMoney() throws Exception {
    }

    @Test
    public void testTransferMoney1() throws Exception {
    }

}