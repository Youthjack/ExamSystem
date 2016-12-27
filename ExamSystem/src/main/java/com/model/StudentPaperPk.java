package com.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by takahiro on 2016/12/26.
 */
@Embeddable
public class StudentPaperPk implements Serializable{
    private int studentId;
    private int paperId;

    public StudentPaperPk() {
    }

    public StudentPaperPk(int studentId, int paperId) {
        this.studentId = studentId;
        this.paperId = paperId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StudentPaperPk) {
            StudentPaperPk pk = (StudentPaperPk)obj;
            if(this.studentId==pk.getStudentId() &&
                    this.paperId==pk.getPaperId())
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.studentId*7+this.paperId+"").hashCode();
    }
}
