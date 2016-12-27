package com.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Jack on 2016/12/27.
 */
@Embeddable
public class ProblemPk implements Serializable{
    int paperId;
    int studentId;
    int questionId;

    ProblemPk() {}

    public ProblemPk(int paperId, int studentId, int questionId) {
        this.paperId = paperId;
        this.studentId = studentId;
        this.questionId = questionId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getPaperId() {

        return paperId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getQuestionId() {
        return questionId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProblemPk) {
            ProblemPk pk = (ProblemPk)obj;
            if(this.studentId==pk.getStudentId() &&
                    this.paperId==pk.getPaperId() && this.questionId == pk.getQuestionId())
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.paperId*7+this.questionId*11+this.studentId+"").hashCode();
    }
}
