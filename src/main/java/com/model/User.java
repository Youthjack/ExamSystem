package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Created by takahiro on 2016/12/21.
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false,unique = true,length = 30)
    private String username;

    @Column(nullable = false,length = 255)
    private String password;

    @Column(nullable = false,length = 10)
    private String identity;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

}
