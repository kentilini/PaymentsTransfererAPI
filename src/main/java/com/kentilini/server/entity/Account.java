package com.kentilini.server.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.util.Objects;

@Entity
@Table(name = "ACCOUNTS")
@NamedQueries({
        @NamedQuery(name = "Account.getUserAccounts", query = "select accounts from Account accounts where " +
                "accounts.owner = :user"),
        @NamedQuery(name = "Account.getAccountsByStatus", query = "select accounts from Account accounts where " +
                "accounts.owner = :user and accounts.status = :status"),
        @NamedQuery(name = "Account.getAccountById", query = "select accounts from Account accounts where " +
                "accounts.id = :id")
})
@XmlRootElement
public class Account {
    @XmlType(name = "account_status")
    @XmlEnum
    public enum AccountStatus {
        @XmlEnumValue("OPEN") OPEN,
        @XmlEnumValue("ARCHIVED") ARCHIVED,
        @XmlEnumValue("BLOCKED") BLOCKED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    @XmlElement(nillable = true, required = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_owner", nullable = false)
    @XmlElement(nillable = false, required = true)
    private User owner;

    @NotNull
    @Column(name = "balance", nullable = false)
    @XmlElement(required = true, nillable = false)
    private Double balance;

    @NotNull
    @Column(name = "account_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @XmlElement(nillable = false, required = true)
    private AccountStatus status;

    public boolean isUserOwner(User user) {
        return user.equals(owner);
    }

    public boolean isPositiveBalance() {
        return balance > 0;
    }

    public boolean isAccountPayble() {
        return isPositiveBalance() && status == AccountStatus.OPEN;
    }

    public boolean isAccountSupportTransfers() {
        return status == AccountStatus.OPEN;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public User getOwner() {
        return owner;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
