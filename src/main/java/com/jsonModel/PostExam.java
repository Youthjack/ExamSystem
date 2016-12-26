package com.jsonModel;

import java.util.Date;

/**
 * Created by Jack on 2016/12/23.
 */
public class PostExam {
    private int paperId;
    private int studentId;
    private String name;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {

        return name;
    }



    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }


    public int getPaperId() {

        return paperId;
    }

    public int getStudentId() {
        return studentId;
    }

}
