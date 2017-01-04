package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Jack on 2016/12/23.
 */
@Entity
public class Exam {

    @EmbeddedId
    private StudentPaperPk pk;

    @Column(nullable = false,length = 255)
    private String name;

    @Column
    private int timeSec;

    @Column
    private Date date;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "student")
    private Student student;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "paper")
    private Paper paper;

    @Column
    private int status = 0;     //0表示还没做，1表示已交卷

    @Column
    private int hasCorrect = 0; //0表示还没改，1表示已改

    @Column
    private int mark = 0;

    public StudentPaperPk getPk() {
        return pk;
    }

    public void setPk(StudentPaperPk pk) {
        this.pk = pk;
    }
/*
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

    public int getTimeSec() {
        return timeSec;
    }

    public void setTimeSec(int timeSec) {
        this.timeSec = timeSec;
    }

    public int getHasCorrect() {
        return hasCorrect;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Paper getPaper() {
        return paper;
    }

    public Student getStudent() {
        return student;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }


    public void setHasCorrect(int hasCorrect) {
        this.hasCorrect = hasCorrect;
    }

}
