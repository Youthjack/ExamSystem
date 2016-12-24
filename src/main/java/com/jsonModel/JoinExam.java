package com.jsonModel;

import java.util.Date;

/**
 * Created by Jack on 2016/12/23.
 */
public class JoinExam {
    private int paperId;
    private int studentId;
    private String name;
    private Date date;
    private int status=0;   //0表示还没开始考，1表示已经考完
    private String result;
    private String timeSec;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTimeSec(String timeSec) {
        this.timeSec = timeSec;
    }

    public String getName() {

        return name;
    }

    public int getStatus() {
        return status;
    }

    public String getTimeSec() {
        return timeSec;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getPaperId() {

        return paperId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getResult() {
        return result;
    }
}
