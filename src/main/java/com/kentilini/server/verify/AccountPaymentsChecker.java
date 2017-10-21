package com.kentilini.server.verify;

import com.kentilini.server.entity.Account;
import com.kentilini.server.exception.MoneyTransferException;

import java.util.Objects;

public class AccountPaymentsChecker {
    private Account from;
    private Account to;
    private Double sum;

    public AccountPaymentsChecker(Account from, Account to, Double sum) {
        this.from = from;
        this.to = to;
        this.sum = sum;
    }

    public AccountPaymentsChecker verifyAccountsStatus(){
        if(!from.isAccountSupportTransfers() || !to.isAccountSupportTransfers()){
            throw new MoneyTransferException("At least one of accounts doesn't support money transferring");
        }
        return this;
    }

    public AccountPaymentsChecker verifyPositiveBalance(){
        if(!from.isPositiveBalance()){
            throw new MoneyTransferException("Account has non positive balance");
        }
        return this;
    }

    public AccountPaymentsChecker verifyEnoughMoney(){
        if(from.getBalance() < sum){
            throw new MoneyTransferException("Account has not enough money");
        }
        return this;
    }

    public AccountPaymentsChecker verifySameAccount(){
        if(!Objects.equals(from.getId(), to.getId())){
            throw new MoneyTransferException("Accounts can't be the same");
        }
        return this;
    }

    @Override
    //Override object for smart lock support
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountPaymentsChecker that = (AccountPaymentsChecker) o;
        if(that.to == null){
            return Objects.equals(that.from, this.from) || Objects.equals(that.from, this.to);
        }else if(this.to == null){
            return Objects.equals(this.from, that.from) || Objects.equals(this.from, that.to);
        }

        return Objects.equals(from, that.from) || Objects.equals(to, that.to) || Objects.equals(from, that.to) || Objects.equals(to, that.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
