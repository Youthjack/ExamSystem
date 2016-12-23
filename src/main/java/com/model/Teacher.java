package com.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Jack on 2016/12/23.
 */
@Entity
public class Teacher {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false,unique = true)
    private String number;
    @Column(nullable = false,length = 20)
    private String name;
    @Column
    private String email;

    public Teacher() {}

    public Teacher(String number, String name, String email) {
        this.number = number;
        this.name = name;
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
