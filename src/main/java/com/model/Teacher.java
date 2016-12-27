package com.model;

import org.hibernate.annotations.Cascade;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

/**
 * Created by takahiro on 2016/12/23.
 */
@Entity
public class Teacher {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false,unique = true)
    private String number;
    @Column(nullable = false,length = 30)
    private String name;
    @Column
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(value = {org.hibernate.annotations.CascadeType.REMOVE})
    @JoinTable(name = "Teacher_Student",joinColumns = {
            @JoinColumn(name = "teacherId",referencedColumnName = "id")},
            inverseJoinColumns ={@JoinColumn(name = "studentId",referencedColumnName = "id")}
    )
    private List<Student> studentList;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> students) {
        this.studentList = students;
    }
}
