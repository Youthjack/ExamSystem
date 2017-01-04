package com.jsonModel;

/**
 * Created by Jack on 2016/12/28.
 */
public class ExamReturn {
    int studentId;
    int paperId;
    String paperName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String username;

    ExamReturn() {}

    public ExamReturn(int studentId, int paperId, String paperName,String username) {
        this.studentId = studentId;
        this.paperId = paperId;
        this.paperName = paperName;
        this.username = username;
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

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }
}
