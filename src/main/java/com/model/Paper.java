package com.model;


import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.List;
import java.util.Set;

/**
 * Created by takahiro on 2016/12/22.
 */
@Entity
public class Paper {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false,length = 255)
    private String paperName;
    @Column
    private String questions;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(value = {CascadeType.REMOVE})
    @JoinTable(name = "Paper_Question",joinColumns = {
            @JoinColumn(name = "paperId",referencedColumnName = "id")},
            inverseJoinColumns ={@JoinColumn(name = "questionId",referencedColumnName = "id")}
    )
    private List<Question>questionSet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public List<Question>getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(List<Question>questionSet) {
        this.questionSet = questionSet;
    }
}
