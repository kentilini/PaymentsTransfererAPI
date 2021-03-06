package com.kentilini.server.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "USER")
@XmlRootElement
@NamedQueries(value = {
        @NamedQuery(name = "User.getAllIds", query = "select user.id from User user"),
        @NamedQuery(name = "User.getById", query = "select user from User user where user.id = :id")})
public class User {
    //ToDo: add one to many meta user info

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement(name = "userId", nillable = true)
    private BigInteger id;

    @Column(name = "NAME", nullable = false, length = 50)
    @XmlElement(name = "name", nillable = false)
    private String name;

    @Column(name = "EMAIL", unique = true, nullable = false, length = 50)
    @XmlElement(name = "email", nillable = false)
    private String email;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<Account>();


    public User() {
        //jackson constructor
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addAccount(Account account) {
        accounts.add(account);
        account.setOwner(this);
    }
}