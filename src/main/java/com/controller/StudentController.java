package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonModel.FinishExam;
import com.jsonModel.Message;
import com.jsonModel.PostExam;
import com.mapper.ExamRepository;
import com.mapper.PaperRepository;
import com.mapper.StudentRepository;
import com.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    ObjectMapper objectMapper=new ObjectMapper();
    Map<Integer,String> ansMap = new HashMap<Integer, String>();
    Map<Integer,Integer> pointMap = new HashMap<Integer, Integer>();

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

    @RequestMapping(value = "/finishExam",method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    @ResponseBody
    public String finishExam(FinishExam finishExam) throws JsonProcessingException {
        Message msg = new Message();
        if(finishExam==null) {
            msg.setStatus("jsonModel cannot be null!");
        } else {
            int paperId = finishExam.getPaperId();
            int studentId = finishExam.getStudentId();
            Exam exam = examRepository.findByPk(new StudentPaperPk(studentId,paperId));
            if(null == exam) {
                msg.setStatus("cannot find such exam!");
            } else {
                exam.setTimeSec(finishExam.getTimeSec());
                List<String> list = finishExam.getAnswerList();
                Paper paper = exam.getPaper();
                Set<Question> questionSet = paper.getQuestionSet();
                if(questionSet==null) {
                    msg.setStatus("no question set");
                } else {
                    ansMap.clear();
                    pointMap.clear();
                    for (Question q : questionSet) {
                        ansMap.put(q.getId(),q.getAnswer());
                        pointMap.put(q.getId(),q.getPoint());
                    }
                    List<Integer> idList = finishExam.getIdList();
                    if (list != null && list.size() > 0 &&
                            list.size()==idList.size()) {
                        StringBuilder sb = new StringBuilder("");
                        int mark = 0;
                        for (int i = 0; i < list.size(); i++) {
                            String ans = ansMap.get(idList.get(i));
                            int point = pointMap.get(idList.get(i));
                            if(list.get(i).equals(ans)) {
                                sb.append("T ");
                                mark += point;
                            } else {
                                sb.append("F ");
                            }
                        }
                        exam.setResult(sb.toString().trim());
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
