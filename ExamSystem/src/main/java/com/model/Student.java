package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;
import java.util.List;

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
    @Column(nullable = false,length = 30)
    private String name;
    @Column(nullable = false,length = 30)
    private String className;

    @ManyToMany(mappedBy = "studentList")
    @JsonIgnore
    private List<Teacher>teachers;

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

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

}
