package com.model;

import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 * Created by Jack on 2016/12/27.
 */
@Entity
public class Problem {
    @EmbeddedId
    ProblemPk pk;
    @Column
    String question;
    @Column
    String myAnswer;
    @Column
    String rightAnswer;
    @Column
    int type = 0;
    @Column
    int mark = 0;


    public void setPk(ProblemPk pk) {
        this.pk = pk;
    }

    public ProblemPk getPk() {
        return pk;
    }


    public void setMark(int mark) {
        this.mark = mark;
    }


    public String getMyAnswer() {
        return myAnswer;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setMyAnswer(String myAnswer) {
        this.myAnswer = myAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public int getMark() {
        return mark;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
