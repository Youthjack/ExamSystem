package com.model;

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
    String answer;
    @Column
    int mark = 0;


    public void setPk(ProblemPk pk) {
        this.pk = pk;
    }

    public ProblemPk getPk() {
        return pk;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }


    public String getAnswer() {
        return answer;
    }

    public int getMark() {
        return mark;
    }
}
