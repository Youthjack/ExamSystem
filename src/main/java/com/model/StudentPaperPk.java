package com.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Jack on 2016/12/23.
 */
//联合主键
@Embeddable
public class StudentPaperPk implements Serializable{

    private int studentId=0;

    private int paperId=0;

    public StudentPaperPk(){}

    public StudentPaperPk(int studentId, int paperId) {
        this.studentId = studentId;
        this.paperId = paperId;
    }

    public int getPaperId() {
        return paperId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
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
