package com.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by takahiro on 2016/12/22.
 */
@Entity
public class Paper {
    @Id
    @GeneratedValue
    private int id;
    @Column
    private String paperName;
    @Column
    private String questions;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Paper_Question",joinColumns = {
            @JoinColumn(name = "paperId",referencedColumnName = "id")},
            inverseJoinColumns ={@JoinColumn(name = "questionId",referencedColumnName = "id")}
    )
    private Set<Question>questionSet;

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
