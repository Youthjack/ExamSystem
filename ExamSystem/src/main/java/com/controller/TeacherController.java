package com.controller;

import com.csvreader.CsvReader;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonModel.Message;
import com.jsonModel.PostExam;
import com.mapper.*;
import com.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by takahiro on 2016/12/22.
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    PaperRepository paperRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    ExamRepository examRepository;

    ObjectMapper objectMapper=new ObjectMapper();

    //添加跟更改都是这个接口

    @RequestMapping(value = "/seeStudents",method = RequestMethod.POST)
    @ResponseBody
    public String seeStudents(@RequestBody String body)throws JsonProcessingException{
        Message message=new Message();
        String json;
        Teacher teacher=new Teacher();
        try{
            teacher=objectMapper.readValue(body,Teacher.class);
        }catch (IOException e){
            System.out.println(e);
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        try {
            Teacher list =teacherRepository.findById(teacher.getId());
            json=objectMapper.writeValueAsString(list);
        }catch (Exception e){
            System.out.println(e);
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
        }
        return json;
    }

    //对数据库题目的操作
    @RequestMapping("/addQuestion")
    @ResponseBody
    public String addQuestion(@RequestBody String body)throws JsonProcessingException{
        Message message=new Message();
        String json;
        Question question;
        try{
            question=objectMapper.readValue(body,Question.class);
        }catch (IOException e){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(question.getChapter()==null||question.getAnswer()==null||question.getPoint()==0||question.getQuestion()==null||
                question.getType()==0){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        try {
            questionRepository.save(question);
            message.setStatus("success");
        }catch (Exception e){
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    @RequestMapping("/seeQuestion")
    @ResponseBody
    public String seeQuestion()throws JsonProcessingException{
        String json;
        List<Question>list=questionRepository.findAll();
        json=objectMapper.writeValueAsString(list);
        return json;
    }

    @RequestMapping("/updateQuestion")
    @ResponseBody
    public String updateQuestion(@RequestBody String body)throws JsonProcessingException{
        Message message=new Message();
        String json;
        Question question;
        try{
            question=objectMapper.readValue(body,Question.class);
        }catch (IOException e){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            System.out.println("卡在解析");
            return json;
        }
        if(question.getChapter()==null||question.getAnswer()==null||question.getPoint()==0||question.getQuestion()==null||
                question.getId()==0){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            System.out.println("卡在数据格式");
            return json;
        }
        try {
            int s=questionRepository.updateQuestion(question.getQuestion(),question.getAnswer(),question.getPoint(),
                    question.getChapter(),question.getId());
            if(s==1) message.setStatus("success");
            else message.setStatus("error");
        }catch (Exception e){
            System.out.println("卡在数据库");
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    @RequestMapping("/deleteQuestion")
    @ResponseBody
    public String deleteQuestion(@RequestBody String body)throws JsonProcessingException{
        Message message=new Message();
        String json;
        Question question;
        try{
            question=objectMapper.readValue(body,Question.class);
        }catch (IOException e){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(question.getId()==0){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        try {
            Question q=new Question();
            q.setId(question.getId());
            questionRepository.delete(q);
            message.setStatus("success");
        }catch (Exception e){
            System.out.println(e);
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }



    //关于试卷的操作
    //其实这里有个很严重的问题没有解决，把试卷显示到前端之后，假如这时有另外一个用户删除了一份试卷，这个用户要想再更新这份试卷
    //是不可能的，会出现个很严重的问题，解决方法可以前端轮询，或者websocket
    @RequestMapping(value = "/addPaper",method = RequestMethod.POST)
    @ResponseBody
    public String addPaper(@RequestBody String body)throws  JsonProcessingException{
        Paper paper;
        Message message=new Message();
        String json;
        try{
            paper=objectMapper.readValue(body,Paper.class);
        }catch (IOException e){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(paper.getPaperName()==null||paper.getQuestions()==null){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        try{
            String[]groups=paper.getQuestions().split("-");
            List<Question>list=new ArrayList<Question>();
            for(int i=0;i<groups.length;i++){
                System.out.println(groups[i]);
                Question question=new Question();
                question.setId(Integer.parseInt(groups[i]));
                list.add(question);
            }
            paper.setQuestionSet(list);
            paperRepository.save(paper);
            message.setStatus("success");
        }catch (Exception e){
            System.out.println(e);
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    //删除试卷时要级联删除paper_question
    @RequestMapping(value = "/deletePaper",method = RequestMethod.POST)
    @ResponseBody
    public String deletePaper(@RequestBody String body)throws JsonProcessingException{
        Paper paper;
        Message message=new Message();
        String json;
        try{
            paper=objectMapper.readValue(body,Paper.class);
        }catch (IOException e){
            System.out.println(e);
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(paper.getId()==0){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        try{
            paperRepository.delete(paper);
            message.setStatus("success");
        }catch (Exception e){
            System.out.println(e);
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    @RequestMapping(value = "/showAllPapers",method = RequestMethod.GET)
    @ResponseBody
    public String showAllPapers()throws JsonProcessingException{
        String json;
        Message message=new Message();
        try{
            List<Paper>list=paperRepository.findAll();
            json=objectMapper.writeValueAsString(list);
            message.setStatus("success");
            return json;
        } catch (Exception e){
            System.out.println(e);
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
    }

    @RequestMapping(value = "/updatePaper",method = RequestMethod.POST)
    @ResponseBody
    public String updatePaper(@RequestBody String body)throws JsonProcessingException{
        String json;
        Message message=new Message();
        Paper paper;
        try{
            paper=objectMapper.readValue(body,Paper.class);
        }catch (IOException e){
            System.out.println(e);
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(paper.getId()==0||paper.getPaperName()==null||paper.getQuestions()==null){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        try {
            String[] groups = paper.getQuestions().split("-");
            List<Question> list = new ArrayList<Question>();
            for (int i = 0; i < groups.length; i++) {
                Question question = new Question();
                question.setId(Integer.parseInt(groups[i]));
                list.add(question);
            }
            paper.setQuestionSet(list);
            paperRepository.save(paper);
            message.setStatus("success");
        }catch (Exception e){
            System.out.println(e);
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    // 以文件csv的形式添加问题到数据库
    //repository.save自带更新功能
    @RequestMapping(value = "/batchAddQuestions",method = RequestMethod.POST)
    @ResponseBody
    public String batchAddQuestion(@RequestParam("file")MultipartFile file)throws IOException{
        Message message=new Message();
        String json;
        if(!file.isEmpty()){
            CsvReader reader=new CsvReader(file.getInputStream(),',', Charset.forName("GBK"));
            reader.readHeaders();
            while(reader.readRecord()){
                Question question=new Question();
                String ques=reader.get(0);
                String ans=reader.get(1);
                int point=Integer.parseInt(reader.get(2));
                String session=reader.get(3);
                int type=Integer.parseInt(reader.get(4));
                question.setAnswer(ans);
                question.setQuestion(ques);
                question.setPoint(point);
                question.setChapter(session);
                question.setType(type);
                questionRepository.save(question);
            }
            message.setStatus("success");
        }else{
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    @RequestMapping(value = "/postExam",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String postExam(@RequestBody String body) throws JsonProcessingException {
        Message msg = new Message();
        String json;
        PostExam postExam;
        try{
            postExam=objectMapper.readValue(body,PostExam.class);
        }catch (IOException e){
            msg.setStatus("error");
            json=objectMapper.writeValueAsString(msg);
            return json;
        }
        if(postExam==null) {
            msg.setStatus("error");
            json = objectMapper.writeValueAsString(msg);
            return json;
        } else {
            if(postExam.getDate()==null||postExam.getName()==null||postExam.getPaperId()==0||postExam.getClassName()==null){
                msg.setStatus("error");
                json=objectMapper.writeValueAsString(msg);
                return json;
            }
            List<Student>list=studentRepository.findByClassName(postExam.getClassName());
            int pid = postExam.getPaperId();
            for(int i=0;i<list.size();i++) {
                int sid;
                try {
                    sid = list.get(i).getId();
                }catch (Exception e){
                    System.out.println(e);
                    msg.setStatus("error");
                    break;
                }
                Student student;
                Paper paper;
                if (null == (student = studentRepository.findById(sid))) {
                    msg.setStatus("student can not find");
                    json = objectMapper.writeValueAsString(msg);
                    return json;
                } else if (null == (paper = paperRepository.findById(pid))) {
                    msg.setStatus("paper can not find");
                    json = objectMapper.writeValueAsString(msg);
                    return json;
                } else {
                    Exam exam = new Exam();
                    StudentPaperPk pk = new StudentPaperPk(sid, pid);
                    exam.setPk(pk);
                    exam.setName(postExam.getName());
                    exam.setStudent(student);
                    exam.setPaper(paper);
                    exam.setDate(postExam.getDate());
                    examRepository.save(exam);
                }
            }
        }
        msg.setStatus("success");
        json = objectMapper.writeValueAsString(msg);
        return json;
    }

    //老师批改试卷的接口
}
