package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonModel.FromSqlSubmit;
import com.jsonModel.Message;
import com.jsonModel.OjQuestionShow;
import com.mapper.OjQuestionRepository;
import com.mapper.PersonRepository;
import com.model.Exam;
import com.model.OjQuestion;
import com.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by takahiro on 2016/12/27.
 */
@RequestMapping("/oj")
@Controller
public class OjController {
    @Autowired
    OjQuestionRepository ojQuestionRepository;
    @Autowired
    PersonRepository personRepository;

    ObjectMapper objectMapper=new ObjectMapper();

    @RequestMapping("/index")
    public String index(){
        return "Index";
    }

    @RequestMapping(value = "/submit",method = RequestMethod.GET)
    public String submit(@PathVariable("id")int id){
        System.out.println(id);
        return "index";
    }

    @RequestMapping(value = "/addOjQuestion",method = RequestMethod.POST)
    @ResponseBody
    public String addOjQuestion(@RequestBody String body)throws  JsonProcessingException{
        OjQuestion ojQuestion;
        Message message=new Message();
        String json;
        try{
            ojQuestion=objectMapper.readValue(body,OjQuestion.class);
        }catch (IOException e){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        ojQuestion.setAcceptance(0.0);
        ojQuestion.setCorrectSubmit(0);
        ojQuestion.setTotalSubmit(0);
        try {
            ojQuestionRepository.save(ojQuestion);
            message.setStatus("success");
        }catch (Exception e){
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    @RequestMapping(value = "/addPerson",method = RequestMethod.GET)
    @ResponseBody
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String addPerson(){
        personRepository.save(new Person("1647023764@qq.com"));
        personRepository.save(new Person("1724827729@qq.com"));
        personRepository.save(new Person("90909090@qq.com"));
        personRepository.save(new Person("1724827729@qq.com"));
        personRepository.save(new Person("1647023764@qq.com"));
        personRepository.save(new Person("123456@163.com"));
        return "success";
    }

    @RequestMapping(value = "/get/{pid}",method = RequestMethod.GET)
    @ResponseBody
    public String get(@PathVariable int pid)throws JsonProcessingException{
        String json="";
        if(pid==0) {
            List<OjQuestion> list = ojQuestionRepository.findAll();
            if(list!=null&&list.size()>0)
                json = objectMapper.writeValueAsString(list);
        }else{
            OjQuestion ojQuestion=ojQuestionRepository.findById(pid);
            if(ojQuestion!=null)
                json=objectMapper.writeValueAsString(ojQuestion);
        }
        return json;
    }

    private static String url="jdbc:mysql://localhost/exam";
    private static String name="com.mysql.jdbc.Driver";
    private static String user="root";
    private static String password="123456";
    @RequestMapping(value = "/submit_auth",method = RequestMethod.POST)
    @ResponseBody
    public String submit_auth(@RequestBody String body)throws JsonProcessingException{
        FromSqlSubmit fromSqlSubmit;
        Message message=new Message();
        String json;
        try{
            fromSqlSubmit=objectMapper.readValue(body,FromSqlSubmit.class);
        }catch (IOException e){
            message.setStatus(e.toString());
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        String sql=fromSqlSubmit.getSql().trim();
        long id=fromSqlSubmit.getId();
        ojQuestionRepository.submit(id);
        if(!sql.contains("select")){
            message.setStatus("提交的不含有select字段");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        Connection conn=null;
        try {
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
            Statement stat = conn.createStatement();
            ResultSet resultSet=stat.executeQuery(sql);
            File file=new File("DuplicateEmail_result.txt");
            if(file.exists()){
                BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line=null;
                while((line=reader.readLine())!=null){
                    if(resultSet.next()==true){
                        String answer=resultSet.getString(1);
                        if(answer.equals(line)) {
                            continue;
                        }else{
                            message.setStatus("wrong");
                            break;
                        }
                    }else{
                        message.setStatus("wrong");
                        break;
                    }
                }
                if(resultSet.next()==true){
                    message.setStatus("wrong");
                }else{
                    message.setStatus("success");
                }
            }else{
                message.setStatus("服务器比对文件不存在");
            }
            conn.rollback();
        }catch (Exception e){
            System.out.println(e);
            message.setStatus(e.toString());
            json=objectMapper.writeValueAsString(message);
        }
        if(message.getStatus().equals("success")){
            ojQuestionRepository.ac(id);
            ojQuestionRepository.refresh(id);
        }else{
            ojQuestionRepository.refresh(fromSqlSubmit.getId());
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

}
