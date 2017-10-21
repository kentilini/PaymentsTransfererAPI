package com.kentilini.server.entity;

import java.math.BigInteger;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "USER")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "User.getAllIds", query = "select user.uid from UserEntity user"),
        @NamedQuery(name = "User.getById", query = "select user from UserEntity user where user.id = :id")})
public class UserEntity {
    //ToDo: add one to many meta user info
    //ToDo: add user payments info

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "NAME", unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "EMAIL", unique = true, nullable = false, length = 50)
    private String email;


    public UserEntity() {
        //jackson constructor
    }

    public UserEntity(String name, String email) {
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
}