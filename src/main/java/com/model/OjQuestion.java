package com.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by takahiro on 2016/12/27.
 */
@Entity
public class OjQuestion {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private double acceptance;
    @Column
    private int correctSubmit;
    @Column()
    private int totalSubmit;
    @Column
    private String difficulty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(double acceptance) {
        this.acceptance = acceptance;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getCorrectSubmit() {
        return correctSubmit;
    }

    public void setCorrectSubmit(int correctSubmit) {
        this.correctSubmit = correctSubmit;
    }

    public int getTotalSubmit() {
        return totalSubmit;
    }

    public void setTotalSubmit(int totalSubmit) {
        this.totalSubmit = totalSubmit;
    }
}
