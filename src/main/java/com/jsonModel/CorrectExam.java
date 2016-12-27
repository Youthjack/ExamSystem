package com.jsonModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2016/12/27.
 */
public class CorrectExam {
    int paperId;
    int studentId;
    List<Integer> questionIdList = new ArrayList<Integer>();
    List<Integer> markList = new ArrayList<Integer>();

    public int getPaperId() {
        return paperId;
    }

    public List<Integer> getQuestionIdList() {
        return questionIdList;
    }

    public void setQuestionIdList(List<Integer> questionIdList) {
        this.questionIdList = questionIdList;
    }

    public List<Integer> getMarkList() {
        return markList;
    }

    public void setMarkList(List<Integer> markList) {
        this.markList = markList;
    }

    public int getStudentId() {
        return studentId;
    }


    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
