package com.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by takahiro on 2016/12/21.
 */
@Entity
public class User{
    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false,unique = true,length = 50)
    private String username;

    @Column(nullable = false,length = 255)
    private String password;

    @Column(nullable = false)
//    private int identity=0; //默认为0
    private String identity;    //ADMIN,STUDENT,TEACHER

    public User() {
    }

    public User(String username, String password, String identity) {
        this.username = username;
        this.password = password;
        this.identity = identity;
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
