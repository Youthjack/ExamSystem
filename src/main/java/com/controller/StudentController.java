package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonModel.FinishExam;
import com.jsonModel.Message;
import com.jsonModel.PostExam;
import com.mapper.ExamRepository;
import com.mapper.PaperRepository;
import com.mapper.ProblemRepository;
import com.mapper.StudentRepository;
import com.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by Jack on 2016/12/23.
 */
@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    ExamRepository examRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    PaperRepository paperRepository;
    @Autowired
    ProblemRepository problemRepository;
    ObjectMapper objectMapper=new ObjectMapper();
    Map<Integer,Question> questionMap = new HashMap<Integer, Question>();

    @RequestMapping(value = "/getExams/{sid}/{pid}",method = RequestMethod.GET)
    @ResponseBody
    public String getExams(@PathVariable int sid,@PathVariable int pid) throws JsonProcessingException {
        String json = "";
        if(pid == 0) {
            List<Exam> exams = examRepository.findByPkStudentId(sid);
            if(exams!= null && exams.size() > 0)
            json = objectMapper.writeValueAsString(exams);
        } else {
            StudentPaperPk pk = new StudentPaperPk(sid,pid);
            Exam exam = examRepository.findByPk(pk);
            if(exam!=null) {
                json = objectMapper.writeValueAsString(exam);
            }
        }
        return json;
    }

    @RequestMapping(value = "/finishExam",method = RequestMethod.POST)
    @ResponseBody
    public String finishExam(@RequestBody FinishExam finishExam) throws JsonProcessingException {
        Message msg = new Message();
        if(finishExam==null) {
            msg.setStatus("jsonModel cannot be null!");
        } else {
            int paperId = finishExam.getPaperId();
            int studentId = finishExam.getStudentId();
            StudentPaperPk pk = new StudentPaperPk(studentId,paperId);
            Exam exam = examRepository.findByPk(pk);
            if(null == exam) {
                msg.setStatus("cannot find such exam!");
            } else {
                List<String> list = finishExam.getAnswerList();
                Paper paper = exam.getPaper();
                Set<Question> questionSet = paper.getQuestionSet();
                if(questionSet==null) {
                    msg.setStatus("no question set");
                } else {
                    questionMap.clear();
                    for (Question q : questionSet) {
                        questionMap.put(q.getId(),q);
                    }
                    List<Integer> idList = finishExam.getIdList();
                    if (list != null && list.size() > 0 &&
                            list.size()==idList.size()) {
                        int mark = 0;
                        boolean hasSubject = false;
                        for (int i = 0; i < list.size(); i++) {
                            Question question = questionMap.get(idList.get(i));
                            int type = question.getType();
                            String ans = question.getAnswer();
                            int point = question.getPoint();
                            String myAns = list.get(i);
                            Problem problem = new Problem();
                            ProblemPk ppk = new ProblemPk(paperId,studentId,idList.get(i));
                            problem.setPk(ppk);
                            problem.setAnswer(myAns);
                            if(type == 1) {     //选择题
                                if(myAns.equals(ans)) {
                                    mark += point;
                                    problem.setMark(point);
                                }
                            } else if(type ==2) {   //问答题
                                hasSubject = true;
                            }
                            problemRepository.save(problem);
                        }
                        if(hasSubject==true) {
                            exam.setHasCorrect(0);
                        } else {                    //如果没有问答题，则当做已经批改完成
                            exam.setHasCorrect(1);
                        }
                        exam.setStatus(1);
                        exam.setMark(mark);
                        examRepository.save(exam);
                        msg.setStatus("success");
                    } else {
                        msg.setStatus("list fault!");
                    }
                }
            }
        }
        return objectMapper.writeValueAsString(msg);
    }

    @RequestMapping("/test")
    @ResponseBody
    public void test() {
        StudentPaperPk pk = new StudentPaperPk(1,1);
        Exam exam = new Exam();
        exam.setPk(pk);
        exam.setName("test");
        exam.setDate(new Date());
        examRepository.save(exam);
    }

}
