package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by takahiro on 2016/12/22.
 */
@Entity
public class Question {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private String question;
    @Column
    private String answer;
    @Column
    private int point;
    @Column
    private String chapter;
    @Column
    private int type;
    @ManyToMany(mappedBy = "questionSet")
    @JsonIgnore
    private List<Paper> papers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String ques) {
        this.question = ques;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String ans) {
        this.answer = ans;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Paper> getPapers() {
        return papers;
    }

    public void setPapers(List<Paper> papers) {
        this.papers = papers;
    }
}
