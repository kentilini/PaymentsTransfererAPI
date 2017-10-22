package com.kentilini.server.verify;

import com.kentilini.server.entity.Account;
import com.kentilini.server.exception.MoneyTransferException;

import java.util.Objects;

public class AccountPaymentsChecker {
    private Account from;
    private Account to;
    private Double amount;

    public AccountPaymentsChecker(Account from, Account to, Double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    /**
     *
     * @return this
     * @throws MoneyTransferException if account doesn't support payments transfers
     */
    public AccountPaymentsChecker verifyAccountsStatus(){
        if(!from.isAccountSupportTransfers() || !to.isAccountSupportTransfers()){
            throw new MoneyTransferException("At least one of accounts doesn't support money transferring");
        }
        return this;
    }

    /**
     *
     * @return this
     * @throws MoneyTransferException if "from" account balance is non positive value
     */
    public AccountPaymentsChecker verifyPositiveBalance(){
        if(!from.isPositiveBalance()){
            throw new MoneyTransferException("Account has non positive balance");
        }
        return this;
    }

    /**
     *
     * @return this
     * @throws MoneyTransferException if amount is greater available balance
     */
    public AccountPaymentsChecker verifyEnoughMoney(){
        if(from.getBalance() < amount){
            throw new MoneyTransferException("Account has not enough money");
        }
        return this;
    }

    /**
     *
     * @return this
     * @throws MoneyTransferException if you trying to transfer money between the same account
     */
    public AccountPaymentsChecker verifySameAccount(){
        if(Objects.equals(from.getId(), to.getId())){
            throw new MoneyTransferException("Accounts can't be the same");
        }
        return this;
    }

    /**
     *
     * @return this
     * @throws MoneyTransferException if amount is not positive double value
     */
    public AccountPaymentsChecker verifyAmount(){
        if(amount < 0){
            throw new MoneyTransferException("Amount should be a positive value");
        }
        return this;
    }

    /**
     *
     * @param o object to check
     * @return true if at least one of this.form or this.to elements equals at least one of that.from or that.to
     */
    @Override
    public boolean equals(Object o) {
        //Override object for JKey lock support.
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
