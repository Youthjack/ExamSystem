package com.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by takahiro on 2016/12/28.
 */
@Entity
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,length = 255)
    private String email;

    public Person() {
    }

    public Person(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
