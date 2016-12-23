package com.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by takahiro on 2016/12/20.
 */
@Entity
public class Student {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false,unique = true)
    private String number;
    @Column(nullable = false,length = 20)
    private String name;
    @Column(nullable = false,length = 20)
    private String className;

    public Student() {}

    public Student(String number, String name, String className) {
        this.number = number;
        this.name = name;
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
