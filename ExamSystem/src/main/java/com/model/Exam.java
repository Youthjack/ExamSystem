package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by takahiro on 2016/12/26.
 */
@Entity
public class Exam  {
    @EmbeddedId
    private StudentPaperPk pk;
    @Column(nullable = false,length = 255)
    private String name;
    @Column
    private int timeSec;
    @Column
    private String result;
    @Column
    private Date date;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "student")
    private Student student;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "paper")
    private Paper paper;

    @Column
    private int status=0;
    @Column
    private int hasCorrect;
    @Column
    private int mark=0;

    public StudentPaperPk getPk() {
        return pk;
    }

    public void setPk(StudentPaperPk pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeSec() {
        return timeSec;
    }

    public void setTimeSec(int timeSec) {
        this.timeSec = timeSec;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
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

    public int getHasCorrect() {
        return hasCorrect;
    }

    public void setHasCorrect(int hasCorrect) {
        this.hasCorrect = hasCorrect;
    }
}
