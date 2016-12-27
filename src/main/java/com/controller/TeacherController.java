package com.controller;

import com.csvreader.CsvReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonModel.CorrectExam;
import com.jsonModel.PostExam;
import com.jsonModel.Message;
import com.mapper.*;
import com.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    ExamRepository examRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    ProblemRepository problemRepository;
    @Autowired
    TeacherRepository teacherRepository;
    ObjectMapper objectMapper=new ObjectMapper();

    //添加跟更改都是这个接口
    @RequestMapping(value = "/addStudents",method = RequestMethod.POST)
    @ResponseBody
    public String addStudent(@RequestBody String body)throws  JsonProcessingException{
        Message message=new Message();
        String json;
        Teacher_Student teacher_student;
        try{
            teacher_student=objectMapper.readValue(body,Teacher_Student.class);
        }catch (IOException e){
            System.out.println(e);
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(teacher_student.getStudentsId()==null||teacher_student.getName()==null||
                teacher_student.getEmail()==null||teacher_student.getNumber()==null){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        try {
            String[] groups = teacher_student.getStudentsId().split("-");
            List<Student> list = new ArrayList<Student>();
            for (String s : groups) {
                Student student = new Student();
                student.setId(Integer.parseInt(s));
                list.add(student);
            }
            Teacher teacher = new Teacher();
            teacher.setId(teacher_student.getTeacherId());
            teacher.setEmail(teacher_student.getEmail());
            teacher.setName(teacher_student.getName());
            teacher.setNumber(teacher_student.getNumber());
            teacher.setStudentList(list);
            teacherRepository.save(teacher);
            message.setStatus("success");
        }catch (Exception e){
            System.out.println(e);
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

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
                System.out.println(reader.get(1)+" "+reader.get(2));
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
    public String postExam(@RequestBody PostExam postExam) throws JsonProcessingException {
        Message msg = new Message();
        String json;
        if(postExam==null) {
            msg.setStatus("error");
            json = objectMapper.writeValueAsString(msg);
        } else {
            int sid = postExam.getStudentId();
            int pid = postExam.getPaperId();
            Student student;
            Paper paper;
            if(null == (student = studentRepository.findById(sid))) {
                msg.setStatus("student can not find");
                json = objectMapper.writeValueAsString(msg);
            } else if(null == (paper=paperRepository.findById(pid))) {
                msg.setStatus("paper can not find");
                json = objectMapper.writeValueAsString(msg);
            } else {
                Exam exam = new Exam();
                StudentPaperPk pk = new StudentPaperPk(sid,pid);
                exam.setPk(pk);
                exam.setName(postExam.getName());
                exam.setStudent(student);
                exam.setPaper(paper);
                exam.setDate(postExam.getDate());
                exam.setTimeSec(postExam.getTimeSec());
                examRepository.save(exam);
                msg.setStatus("success");
                json = objectMapper.writeValueAsString(msg);
            }
        }
        return json;
    }

    @RequestMapping(value = "/correctExam",method = RequestMethod.POST)
    @ResponseBody
    public String correctExam(@RequestBody CorrectExam correctExam) throws JsonProcessingException {
        Message msg = new Message();
        if(correctExam==null) {
            msg.setStatus("param cannot be null!");
        } else {
            int paperId = correctExam.getPaperId();
            int studentId = correctExam.getStudentId();
            Exam exam = examRepository.findByPk(new StudentPaperPk(studentId,paperId));
            if(exam == null) {
                msg.setStatus("exam cannot find!");
            } else {
                int mark = exam.getMark();
                List<Integer> qidList = correctExam.getQuestionIdList();
                List<Integer> markList = correctExam.getMarkList();
                if(qidList==null||markList==null||qidList.size()!=markList.size()) {
                    msg.setStatus("param fault");
                } else {
                    for (int i=0;i<qidList.size();i++) {
                        ProblemPk pk = new ProblemPk(paperId, studentId, qidList.get(i));
                        Problem problem = problemRepository.findByPk(pk);
                        if (null == problem) {
                            msg.setStatus("problem cannot find!");
                        } else {
                            int m = markList.get(i);
                            problem.setMark(m);
                            problemRepository.save(problem);
                            mark += m;
                        }
                    }
                }
                exam.setMark(mark);
                examRepository.save(exam);
                msg.setStatus("success");
            }
        }
        return objectMapper.writeValueAsString(msg);
    }
}
