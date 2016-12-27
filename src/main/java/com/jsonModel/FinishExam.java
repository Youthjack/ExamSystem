package com.jsonModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2016/12/26.
 */
public class FinishExam {
    int paperId;
    int studentId;
    List<String> answerList = new ArrayList<String>();
    List<Integer> idList = new ArrayList<Integer>();

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }


    public int getPaperId() {

        return paperId;
    }

    public int getStudentId() {
        return studentId;
    }

    public List<String> getAnswerList() {
        return answerList;
    }


    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
            this.idList = idList;
    }
}
