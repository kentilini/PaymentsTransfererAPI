package com.kentilini.server.rest;

import com.kentilini.server.entity.Account;
import com.kentilini.server.entity.User;
import com.kentilini.server.service.AccountService;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountRestControllerTest {
    @InjectMocks
    AccountRestController restController;

    @Mock
    AccountService accountService;

    @Mock
    private Account account;

    @BeforeClass
    private void init(){
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    private void tearDown(){
        reset(accountService);
    }

    @Test(groups = "mock")
    public void testAddOrUpdateAccount() throws Exception {
        when(accountService.saveOrUpdate(any(Account.class))).thenReturn(account);

        assertEquals(account, restController.addOrUpdateAccount(account));

        verify(accountService, times(1)).saveOrUpdate(eq(account));
    }

    @Test(groups = "mock")
    public void testAddUserAccount() throws Exception {
        when(accountService.createNewAccountForUser(any(BigInteger.class))).thenReturn(account);

        assertEquals(account, restController.addUserAccount(BigInteger.valueOf(1L)));

        verify(accountService, times(1)).createNewAccountForUser(any(BigInteger.class));
    }



    @Test(groups = "mock")
    public void testBlockAccount() throws Exception {
        when(accountService.changeAccountStatus(any(Long.class), any(Account.AccountStatus.class))).thenReturn(account);

        assertEquals(account, restController.blockAccount(1L));

        verify(accountService, times(1)).changeAccountStatus(1L, Account.AccountStatus.BLOCKED);
    }

    @Test(groups = "mock")
    public void testArchiveAccount() throws Exception {
        when(accountService.changeAccountStatus(any(Long.class), any(Account.AccountStatus.class))).thenReturn(account);

        assertEquals(account, restController.archiveAccount(1L));

        verify(accountService, times(1)).changeAccountStatus(1L, Account.AccountStatus.ARCHIVED);
    }

    @Test(groups = "mock")
    public void testRenewAccount() throws Exception {
        when(accountService.changeAccountStatus(any(Long.class), any(Account.AccountStatus.class))).thenReturn(account);

        assertEquals(account, restController.renewAccount(1L));

        verify(accountService, times(1)).changeAccountStatus(1L, Account.AccountStatus.OPEN);
    }

    @Test(groups = "mock")
    public void testReplenishAccount() throws Exception {
        when(accountService.replenish(any(Long.class), any(Double.class))).thenReturn(account);

        assertEquals(account, restController.replenishAccount(1L, 10d));

        verify(accountService, times(1)).replenish(1L, 10d);
    }

    @Test(groups = "mock")
    public void testGetUserAccounts() throws Exception {
        User user = mock(User.class);
        when(accountService.getUserAccounts(any(User.class))).thenReturn(Collections.singletonList(account));

        assertEquals(1, restController.getUserAccounts(user).size());

        verify(accountService, times(1)).getUserAccounts(user);
    }

    @Test(groups = "mock")
    public void testProvideTransaction() throws Exception {
        doNothing().when(accountService).transferMoney(any(Long.class), any(Long.class), any(Double.class));

        restController.provideTransaction(1L, 1L, 0d);

        verify(accountService, times(1)).transferMoney(1l, 1l, 0d);
    }

}