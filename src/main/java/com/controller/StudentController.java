package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonModel.JoinExam;
import com.jsonModel.Message;
import com.mapper.ExamRepository;
import com.mapper.PaperRepository;
import com.mapper.StudentRepository;
import com.model.Exam;
import com.model.Paper;
import com.model.Student;
import com.model.StudentPaperPk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

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

    @RequestMapping("/test")
    @ResponseBody
    public void test() {
        Student student = new Student();
        student.setName("3231223");
        student.setClassName("321312321");
        student.setNumber("2131232");
        Paper paper = paperRepository.findById(1);
        StudentPaperPk pk = new StudentPaperPk(1,2);
        Exam exam = new Exam();
        exam.setPk(pk);
        exam.setName("test");
        exam.setStudent(student);
        exam.setPaper(paper);
        examRepository.save(exam);
    }

}
