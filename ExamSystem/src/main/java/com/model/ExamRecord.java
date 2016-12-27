package com.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 * Created by takahiro on 2016/12/27.
 */
@Entity
public class ExamRecord {
    @EmbeddedId
    private StudentPaperPk pk;
    @Column
    private int questionId;
    @Column
    private String answer;
    @Column
    private int mark;

    public StudentPaperPk getPk() {
        return pk;
    }

    public void setPk(StudentPaperPk pk) {
        this.pk = pk;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
