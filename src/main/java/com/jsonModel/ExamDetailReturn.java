package com.jsonModel;

/**
 * Created by Jack on 2016/12/28.
 */
public class ExamDetailReturn {
    String questionDescription;
    int questionId;
    String studentAnswer;
    String standardAnswer;
    ExamDetailReturn() {}

    public ExamDetailReturn(String questionDescription, int questionId, String studentAnswer, String standardAnswer) {
        this.questionDescription = questionDescription;
        this.questionId = questionId;
        this.studentAnswer = studentAnswer;
        this.standardAnswer = standardAnswer;
    }

    public String getQuestionDescription() {

        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getStandardAnswer() {
        return standardAnswer;
    }

    public void setStandardAnswer(String standardAnswer) {
        this.standardAnswer = standardAnswer;
    }
}
