package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonModel.FinishExam;
import com.jsonModel.Message;
import com.jsonModel.ChangePwd;
import com.mapper.*;
import com.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * Created by Jack on 2016/12/23.
 */
@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    UserRepository userRepository;
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
        ChangePwd changePwd;
        String json;
        Message message=new Message();
        try{
            changePwd=objectMapper.readValue(body,ChangePwd.class);
        }catch (IOException e){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(changePwd.getUsername()==null||changePwd.getBeforePassword()==null
                ||changePwd.getPassword()==null){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(null == userRepository.findByUsernameAndPassword(changePwd.getUsername(),changePwd.getBeforePassword())) {
            message.setStatus("password error");
            json = objectMapper.writeValueAsString(message);
            return json;
        }
        int s=userRepository.updateUserByUsername(changePwd.getUsername(),changePwd.getPassword());
        if(s==1)    message.setStatus("success");
        else    message.setStatus("error");
        json=objectMapper.writeValueAsString(message);
        return json;
    }

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

    @RequestMapping(value = "/getPaper/{sid}/{pid}",method = RequestMethod.GET)
    @ResponseBody
    public String getPaper(@PathVariable int sid,@PathVariable int pid) throws JsonProcessingException {
        Exam exam = examRepository.findByPk(new StudentPaperPk(sid,pid));
        List<Question> questions = exam.getPaper().getQuestionSet();
        return objectMapper.writeValueAsString(questions);
    }

    @RequestMapping(value = "/getHistoryExam/{sid}/{pid}",method = RequestMethod.GET)
    @ResponseBody
    public String getHistoryExam(@PathVariable int sid,@PathVariable int pid) throws JsonProcessingException {
        List<Problem> problems = problemRepository.findByPkStudentIdAndPkPaperId(sid,pid);
        return objectMapper.writeValueAsString(problems);
    }

    @RequestMapping(value = "/finishExam",method = RequestMethod.POST,
    consumes = "application/json")
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
                List<Question> questionSet = paper.getQuestionSet();
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
                            problem.setMyAnswer(myAns);
                            problem.setRightAnswer(ans);
                            problem.setType(type);
                            problem.setQuestion(question.getQuestion());
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
