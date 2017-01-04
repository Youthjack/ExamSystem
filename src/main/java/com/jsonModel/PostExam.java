package com.jsonModel;

import java.util.Date;

/**
 * Created by takahiro on 2016/12/26.
 */
public class PostExam {
    private int paperId;
    private String className;
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

    public int getPaperId() {
        return paperId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
