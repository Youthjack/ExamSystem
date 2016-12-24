package com.model;

import javax.persistence.*;
import java.util.Set;

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
    @Column(nullable = false,length = 50)
    private String name;
    @Column(nullable = false,length = 50)
    private String className;

    @OneToMany(mappedBy = "student",fetch = FetchType.EAGER)
    private Set<Exam> examSet;

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

    public void setExamSet(Set<Exam> examSet) {
        this.examSet = examSet;
    }

    public Set<Exam> getExamSet() {
        return examSet;
    }
}
