package com.kentilini.server.verify;

import com.kentilini.server.entity.Account;
import com.kentilini.server.exception.MoneyTransferException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class AccountPaymentsCheckerTest {
    private AccountPaymentsChecker paymentsChecker;

    @Mock
    private Account from;

    @Mock
    private Account to;

    @BeforeClass
    private void init(){
        MockitoAnnotations.initMocks(this);
        paymentsChecker = new AccountPaymentsChecker(from, to, 0d);
    }

    @AfterMethod
    private void tearDown(){
        reset(from, to);
    }

    @Test(groups = "mock")
    public void testVerifyAccountsStatus_success() throws Exception {
        when(from.isAccountSupportTransfers()).thenReturn(true);
        when(to.isAccountSupportTransfers()).thenReturn(true);

        assertEquals(paymentsChecker, paymentsChecker.verifyAccountsStatus());

        verify(from, times(1)).isAccountSupportTransfers();
        verify(to, times(1)).isAccountSupportTransfers();
    }

    @Test(groups = "mock", expectedExceptions = MoneyTransferException.class)
    public void testVerifyAccountsStatus_errorFromParam() throws Exception {
        when(from.isAccountSupportTransfers()).thenReturn(false);

        paymentsChecker.verifyAccountsStatus();
    }

    @Test(groups = "mock", expectedExceptions = MoneyTransferException.class)
    public void testVerifyAccountsStatus_errorToParam() throws Exception {
        when(from.isAccountSupportTransfers()).thenReturn(true);
        when(to.isAccountSupportTransfers()).thenReturn(false);

        paymentsChecker.verifyAccountsStatus();
    }

    @Test(groups = "mock")
    public void testVerifyPositiveBalance_success() throws Exception {
        when(from.isPositiveBalance()).thenReturn(true);

        assertEquals(paymentsChecker, paymentsChecker.verifyPositiveBalance());

        verify(from, times(1)).isPositiveBalance();
    }

    @Test(groups = "mock", expectedExceptions = MoneyTransferException.class)
    public void testVerifyPositiveBalance_error() throws Exception {
        when(from.isPositiveBalance()).thenReturn(false);

        paymentsChecker.verifyPositiveBalance();
    }

    @Test(groups = "mock")
    public void testVerifyEnoughMoney_success() throws Exception {
        Double amount = 10d;
        when(from.getBalance()).thenReturn(amount + 1);
        AccountPaymentsChecker paymentsChecker = new AccountPaymentsChecker(from, to, amount);

        assertEquals(paymentsChecker, paymentsChecker.verifyEnoughMoney());

        verify(from, times(1)).getBalance();
    }

    @Test(groups = "mock", expectedExceptions = MoneyTransferException.class)
    public void testVerifyEnoughMoney_error() throws Exception {
        when(from.getBalance()).thenReturn(-1d);

        paymentsChecker.verifyEnoughMoney();
    }

    @Test(groups = "mock")
    public void testVerifySameAccount_success() throws Exception {
        when(from.getId()).thenReturn(1L);
        when(to.getId()).thenReturn(2L);

        assertEquals(paymentsChecker, paymentsChecker.verifySameAccount());

        verify(from, times(1)).getId();
        verify(to, times(1)).getId();
    }

    @Test(groups = "mock", expectedExceptions = MoneyTransferException.class)
    public void testVerifySameAccount_error() throws Exception {
        when(from.getId()).thenReturn(1L);
        when(to.getId()).thenReturn(1L);

        paymentsChecker.verifySameAccount();
    }

    @Test(groups = "mock")
    public void testVerifyAmount_success() throws Exception {
        assertEquals(paymentsChecker, paymentsChecker.verifyAmount());
    }

    @Test(groups = "mock", expectedExceptions = MoneyTransferException.class)
    public void testVerifyAmount_error() throws Exception {
        AccountPaymentsChecker paymentsChecker = new AccountPaymentsChecker(from, to, -1d);

        assertEquals(paymentsChecker, paymentsChecker.verifyAmount());
    }

    @Test(groups = "mock")
    public void testEquals_fromElementHaveSameId_success() throws Exception {
        Account thatFrom = mock(Account.class);
        when(thatFrom.getId()).thenReturn(1L);
        Account thatTo = mock(Account.class);
        when(thatTo.getId()).thenReturn(10L);

        when(from.getId()).thenReturn(1L);
        when(to.getId()).thenReturn(2L);

        assertTrue(paymentsChecker.equals(new AccountPaymentsChecker(thatFrom, thatTo, 10.0d)));

        verify(to, atLeastOnce()).getId();
        verify(from, atLeastOnce()).getId();
    }

    @Test(groups = "mock")
    public void testEquals_fromElementHaveSameToId_success() throws Exception {
        Account thatFrom = mock(Account.class);
        when(thatFrom.getId()).thenReturn(1L);
        Account thatTo = mock(Account.class);
        when(thatTo.getId()).thenReturn(100L);

        when(from.getId()).thenReturn(10L);
        when(to.getId()).thenReturn(1L);

        assertTrue(paymentsChecker.equals(new AccountPaymentsChecker(thatFrom, thatTo, 10.0d)));

        verify(to, atLeastOnce()).getId();
        verify(from, atLeastOnce()).getId();
    }

    @Test(groups = "mock")
    public void testEquals_fail() throws Exception {
        Account thatFrom = mock(Account.class);
        when(thatFrom.getId()).thenReturn(1L);
        Account thatTo = mock(Account.class);
        when(thatTo.getId()).thenReturn(10L);

        when(from.getId()).thenReturn(2L);
        when(to.getId()).thenReturn(5L);

        assertFalse(paymentsChecker.equals(new AccountPaymentsChecker(thatFrom, thatTo, 10.0d)));

        verify(to, atLeastOnce()).getId();
        verify(from, atLeastOnce()).getId();
    }

    @Test(groups = "mock")
    public void testEquals_nullTo_success() throws Exception {
        Account thatFrom = mock(Account.class);
        when(thatFrom.getId()).thenReturn(1L);

        when(from.getId()).thenReturn(2L);
        when(to.getId()).thenReturn(1L);

        assertTrue(paymentsChecker.equals(new AccountPaymentsChecker(thatFrom, null, 10.0d)));

        verify(to, atLeastOnce()).getId();
        verify(from, atLeastOnce()).getId();
    }

    @Test(groups = "mock")
    public void testEquals_nullFrom_fail() throws Exception {
        Account thatTo = mock(Account.class);
        when(thatTo.getId()).thenReturn(1L);

        when(from.getId()).thenReturn(1L);
        when(to.getId()).thenReturn(1L);

        assertFalse(paymentsChecker.equals(new AccountPaymentsChecker(null, thatTo, 10.0d)));
    }
}