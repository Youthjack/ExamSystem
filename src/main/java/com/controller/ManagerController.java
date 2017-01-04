package com.controller;

import com.annotation.Authorization;
import com.csvreader.CsvReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonModel.FromAddManager;
import com.jsonModel.Message;
import com.jsonModel.ToManagerSee;
import com.mapper.StudentRepository;
import com.mapper.TeacherRepository;
import com.mapper.UserRepository;
import com.model.Student;
import com.model.Teacher;
import com.jsonModel.Teacher_Student;
import com.model.User;
import com.model.basicEnum.IdentifyType;
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
import java.util.List;

/**
 * Created by takahiro on 2016/12/21.
 */
@Controller
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    ObjectMapper objectMapper=new ObjectMapper();

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    @Authorization
    public String index(){
        return "Manager_see";
    }

    @RequestMapping(value = "/see",method = RequestMethod.GET)
    @ResponseBody
    public String see()throws JsonProcessingException{
        String json;
        List<User>list=userRepository.findAll();
        List<ToManagerSee>res=new ArrayList<ToManagerSee>();
        for(int i=0;i<list.size();i++){
            User user=list.get(i);
            ToManagerSee obj=new ToManagerSee();
            obj.setId(user.getId());
            obj.setUsername(user.getUsername());
            obj.setIdentity(user.getIdentity());
            if(user.getIdentity().equals(IdentifyType.STUDENT)) {
                Student student=studentRepository.findByNumber(user.getUsername());
                obj.setId(student.getId());
                obj.setName(student.getName());
                obj.setClassName(student.getClassName());
            }else if(user.getIdentity().equals(IdentifyType.TEACHER)){
                Teacher teacher=teacherRepository.findByNumber(user.getUsername());
                obj.setId(teacher.getId());
                obj.setName(teacher.getName());
                obj.setEmail(teacher.getEmail());
            }else if(user.getIdentity().equals(IdentifyType.ADMIN)){
                ;
            }
            res.add(obj);
        }
        json=objectMapper.writeValueAsString(res);
        return json;
    }

    @RequestMapping(value = "/batchAddUser",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String batchAddUser(@RequestParam("file")MultipartFile file)throws IOException{
        Message message=new Message();
        String json;
        if(!file.isEmpty()){
            CsvReader reader=new CsvReader(file.getInputStream(),',', Charset.forName("GBK"));
            reader.readHeaders();
            while(reader.readRecord()){
                String username=reader.get(0);
                String password=reader.get(1);
                String identity=reader.get(2);
                User user=new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setIdentity(identity);
                if(identity.equals(IdentifyType.STUDENT)){
                    Student student=new Student();
                    student.setNumber(username);
                    student.setName(reader.get(3));
                    student.setClassName(reader.get(4));
                    userRepository.save(user);
                    studentRepository.save(student);
                }else if(identity.equals(IdentifyType.TEACHER)){
                    Teacher teacher=new Teacher();
                    teacher.setNumber(username);
                    teacher.setName(reader.get(3));
                    teacher.setEmail(reader.get(4));
                    userRepository.save(user);
                    teacherRepository.save(teacher);
                }else if(identity.equals(IdentifyType.ADMIN)){
                    userRepository.save(user);
                }
            }
        }else{
            message.setStatus("error");
        }
        message.setStatus("success");
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    //还没有测试
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String add(@RequestBody String body)throws JsonProcessingException{
        Message message=new Message();
        String json;
        FromAddManager fromAddManager;
        try {
            fromAddManager = objectMapper.readValue(body, FromAddManager.class);
        }catch (IOException e){
            System.out.println(e);
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(fromAddManager.getUsername()==null||fromAddManager.getPassword()==null||fromAddManager.getIdentity()==null) {
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        try{
            User user=new User();
            user.setUsername(fromAddManager.getUsername());
            user.setPassword(fromAddManager.getPassword());
            user.setIdentity(fromAddManager.getIdentity());
            if(fromAddManager.getIdentity().equals(IdentifyType.STUDENT)){
                Student student=new Student();
                student.setClassName(fromAddManager.getClassName());
                student.setName(fromAddManager.getName());
                student.setNumber(fromAddManager.getNumber());
                userRepository.save(user);
                studentRepository.save(student);        //非空还是可以插入的？?
            }else if(fromAddManager.getIdentity().equals(IdentifyType.TEACHER)){
                Teacher teacher=new Teacher();
                teacher.setNumber(fromAddManager.getNumber());
                teacher.setName(fromAddManager.getName());
                teacher.setEmail(fromAddManager.getEmail());
                userRepository.save(user);
                teacherRepository.save(teacher);
            }else{
                userRepository.save(user);
            }
            message.setStatus("success");
        }catch (Exception e){
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String update(@RequestBody String body)throws JsonProcessingException{
        Message message=new Message();
        String json;
        FromAddManager fromAddManager=new FromAddManager();
        try {
            fromAddManager = objectMapper.readValue(body, FromAddManager.class);
        }catch (IOException e){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(fromAddManager.getUsername()==null||fromAddManager.getPassword()==null||fromAddManager.getIdentity()==null) {
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        User user=new User();
        user.setUsername(fromAddManager.getUsername());
        user.setPassword(fromAddManager.getPassword());
        user.setIdentity(fromAddManager.getIdentity());
        if(fromAddManager.getIdentity().equals(IdentifyType.STUDENT)){
            Student student=new Student();
            student.setClassName(fromAddManager.getClassName());
            student.setName(fromAddManager.getName());
            student.setNumber(fromAddManager.getNumber());
            student.setId(fromAddManager.getId());
            int s=userRepository.updateUser(user.getUsername(),user.getPassword(),user.getIdentity());
            int s1=studentRepository.updateStudentByNumber(student.getName(),student.getClassName(),student.getNumber());
            if(s==1&&s1==1) message.setStatus("success");
            else message.setStatus("error");
        }else if(fromAddManager.getIdentity().equals(IdentifyType.TEACHER)){
            Teacher teacher=new Teacher();
            teacher.setNumber(fromAddManager.getNumber());
            teacher.setName(fromAddManager.getName());
            teacher.setEmail(fromAddManager.getEmail());
            teacher.setId(fromAddManager.getId());
            int s=userRepository.updateUser(user.getUsername(),user.getPassword(),user.getIdentity());
            int s1=teacherRepository.updateTeacherByNumber(teacher.getName(),teacher.getEmail(),teacher.getNumber());
            if(s==1&&s1==1) message.setStatus("success");
            else message.setStatus("error");
        }else{
            int s=userRepository.updateUser(user.getUsername(),user.getPassword(),user.getIdentity());
            if(s==1) message.setStatus("success");
            else message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String delete(@RequestBody String body)throws JsonProcessingException{
        String json;
        Message message=new Message();
        User user;
        try{
            user=objectMapper.readValue(body,User.class);
        }catch (IOException e){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(user.getUsername()==null){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        try {
            if(user.getIdentity().equals(IdentifyType.STUDENT)) {
                int s=userRepository.deleteUserByUsername(user.getUsername());
                int s1=studentRepository.deleteStudentByNumber(user.getUsername());
                if(s==1&&s1==1)  message.setStatus("success");
                else message.setStatus("error");
            }else if(user.getIdentity().equals(IdentifyType.TEACHER)){
                int s=userRepository.deleteUserByUsername(user.getUsername());
                int s1=teacherRepository.deleteTeacherByNumber(user.getUsername());
                if(s==1&&s1==1)  message.setStatus("success");
                else message.setStatus("error");
            }else{
                int s=userRepository.deleteUserByUsername(user.getUsername());
                if(s==1)    message.setStatus("success");
                else message.setStatus("error");
            }
            message.setStatus("success");
        }catch (Exception e){
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    @RequestMapping(value = "/addStudents",method = RequestMethod.POST)
    @ResponseBody
    public String addStudents(@RequestBody String body) throws JsonProcessingException{
        Message message=new Message();
        String json;
        Teacher_Student teacher_student;
        try{
            teacher_student=objectMapper.readValue(body,Teacher_Student.class);
        } catch (IOException e){
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
            String[] groups = teacher_student.getStudentsId().split("_");
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
}
