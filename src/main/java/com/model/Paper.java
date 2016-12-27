package com.model;

import javax.persistence.*;
import javax.persistence.CascadeType;
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
    @Column(nullable = false,length = 50)
    private String paperName;
    @Column
    private String questions;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "Paper_Question",
            joinColumns = {@JoinColumn(name = "paperId",referencedColumnName = "id")},
            inverseJoinColumns ={@JoinColumn(name = "questionId",referencedColumnName = "id")}
    )
    private List<Question> questionSet;

    @OneToMany(mappedBy = "paper",fetch = FetchType.EAGER)
    private Set<Exam> examSet;


    public Paper() {}

    public Paper(String paperName, String questions, List<Question> questionSet) {
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

    public void setQuestionSet(List<Question> questionSet) {
        this.questionSet = questionSet;
    }

    public List<Question> getQuestionSet() {
        return questionSet;
    }

    public void setExamSet(Set<Exam> examSet) {
        this.examSet = examSet;
    }

    public Set<Exam> getExamSet() {
        return examSet;
    }
}
