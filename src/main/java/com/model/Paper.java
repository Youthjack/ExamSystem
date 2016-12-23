package com.model;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Set;

/**
 * Created by takahiro on 2016/12/22.
 */
@Entity
public class Paper {
    @Id
    @GeneratedValue
    @Column(name = "paperId")
    private int id;
    @Column(nullable = false,length = 50)
    private String paperName;
    @Column
    private String questions;
    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(value = {CascadeType.SAVE_UPDATE, CascadeType.PERSIST,
            CascadeType.REMOVE,CascadeType.MERGE})
    @JoinTable(name = "Paper_Question",joinColumns = {
            @JoinColumn(name = "paperId")},
            inverseJoinColumns ={@JoinColumn(name = "questionId")}
    )
    private Set<Question>questionSet;

    public Paper() {}

    public Paper(String paperName, String questions, Set<Question> questionSet) {
        this.paperName = paperName;
        this.questions = questions;
        this.questionSet = questionSet;
    }

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

    public Set<Question> getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(Set<Question> questionSet) {
        this.questionSet = questionSet;
    }
}
