package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonModel.FinishExam;
import com.jsonModel.Message;
import com.mapper.ExamRepository;
import com.mapper.PaperRepository;
import com.mapper.StudentRepository;
import com.mapper.UserRepository;
import com.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by takahiro on 2016/12/26.
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
    UserRepository userRepository;
    ObjectMapper objectMapper=new ObjectMapper();

    @RequestMapping(value = "/info",method = RequestMethod.POST)
    @ResponseBody
    public String info(@RequestBody String body)throws JsonProcessingException{
        Student student;
        String json;
        Message message=new Message();
        try{
            student=objectMapper.readValue(body,Student.class);
        }catch (IOException e){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(student.getId()==0){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        Student student1=studentRepository.findById(student.getId());
        json=objectMapper.writeValueAsString(student1);
        return json;
    }

    @RequestMapping(value = "/changePwd",method = RequestMethod.POST)
    @ResponseBody
    public String changePwd(@RequestBody String body)throws JsonProcessingException{
        User user;
        String json;
        Message message=new Message();
        try{
            user=objectMapper.readValue(body,User.class);
        }catch (IOException e){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(user.getUsername()==null||user.getPassword()==null){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        int s=userRepository.updateUserByUsername(user.getUsername(),user.getPassword());
        if(s==1)    message.setStatus("success");
        else    message.setStatus("error");
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    @RequestMapping(value = "/getExams/{sid}/{pid}",method = RequestMethod.GET)
    @ResponseBody
    public String getExams(@PathVariable int pid,@PathVariable int sid)throws JsonProcessingException{
        String json="";
        if(pid==0){
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

    @RequestMapping(value = "/enterExam",method = RequestMethod.POST)
    @ResponseBody
    public String enterExam(@RequestBody String body)throws  JsonProcessingException{
        StudentPaperPk studentPaperPk;
        String json;
        Message message=new Message();
        try{
            studentPaperPk=objectMapper.readValue(body,StudentPaperPk.class);
        }catch (IOException e){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        Exam exam=examRepository.findByPk(studentPaperPk);
        if(exam==null||exam.getStatus()==1){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        Paper paper=paperRepository.findById(studentPaperPk.getPaperId());
        json=objectMapper.writeValueAsString(paper);
        return json;
    }

    @RequestMapping(value = "/finishExam",method = RequestMethod.POST)
    @ResponseBody
    public String finishExam(@RequestBody String body)throws  JsonProcessingException{
        FinishExam finishExam;
        Message msg = new Message();
        String json;
        try{
            finishExam=objectMapper.readValue(body,FinishExam.class);
        }catch (IOException e){
            msg.setStatus("error");
            json=objectMapper.writeValueAsString(msg);
            return json;
        }
        Map<Integer,String> ansMap = new HashMap<Integer, String>();
        Map<Integer,Integer> pointMap = new HashMap<Integer, Integer>();
        if(finishExam==null) {
            msg.setStatus("jsonModel cannot be null!");
        } else {
            int paperId = finishExam.getPaperId();
            int studentId = finishExam.getStudentId();
            Exam exam = examRepository.findByPk(new StudentPaperPk(studentId, paperId));
            if (null == exam) {
                msg.setStatus("cannot find such exam!");
            } else {
                exam.setTimeSec(finishExam.getTimeSec());
                List<String> answerList = finishExam.getAnswerList();
                Paper paper = exam.getPaper();
                List<Question> questionSet = paper.getQuestionSet();
                if (questionSet == null) {
                    msg.setStatus("no question set");
                } else {
                    ansMap.clear();
                    pointMap.clear();
                    for (Question q : questionSet) {
                        ansMap.put(q.getId(), q.getAnswer());
                        pointMap.put(q.getId(), q.getPoint());
                    }
                    List<Integer> idList = finishExam.getIdList();
                    if (answerList != null && answerList.size() > 0 &&
                            answerList.size() == idList.size()) {
                        StringBuilder sb = new StringBuilder("");
                        int mark = 0;
                        for (int i = 0; i < answerList.size(); i++) {
                            String ans = ansMap.get(idList.get(i));
                            int point = pointMap.get(idList.get(i));
                            if (answerList.get(i).equals(ans)) {
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



}
