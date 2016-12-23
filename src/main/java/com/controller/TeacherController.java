package com.controller;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonModel.Message;
import com.mapper.PaperRepository;
import com.mapper.QuestionRepository;
import com.model.Paper;
import com.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

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
    ObjectMapper objectMapper=new ObjectMapper();

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
        if(question.getSession()==null||question.getAns()==null||question.getPoint()==0||question.getQues()==null||
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
        if(question.getSession()==null||question.getAns()==null||question.getPoint()==0||question.getQues()==null||
                question.getId()==0){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            System.out.println("卡在数据格式");
            return json;
        }
        try {
            questionRepository.updateQuestion(question.getQues(),question.getAns(),question.getPoint(),
                    question.getSession(),question.getId());
            message.setStatus("success");
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
            questionRepository.deleteQuestionById(question.getId());
            message.setStatus("success");
        }catch (Exception e){
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    @RequestMapping("/addPaper")
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
            paperRepository.save(paper);
            message.setStatus("success");
        }catch (Exception e){
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    @RequestMapping("/deletePaper")
    @ResponseBody
    public String deletePaper(@RequestBody String body)throws JsonProcessingException{
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
        if(paper.getId()==0){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        try{
            paperRepository.deletePaperById(paper.getId());
            message.setStatus("success");
        }catch (Exception e){
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    //怎样查询两个表的数据并返回
    @RequestMapping("/showAllPapers")
    @ResponseBody
    public String showAllPapers()throws JsonProcessingException{
        String json;
        Message message=new Message();
        List<Paper>list=paperRepository.findAllPaperQuestion();
        try{
            json=objectMapper.writeValueAsString(list);
            message.setStatus("success");
            return json;
        } catch (Exception e){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
    }

    // 以文件csv的形式添加到数据库
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
                question.setAns(ans);
                question.setQues(ques);
                question.setPoint(point);
                question.setSession(session);
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
}
