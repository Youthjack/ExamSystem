package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonModel.OjQuestionShow;
import com.mapper.OjQuestionRepository;
import com.model.OjQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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


    @RequestMapping("/get")
    @ResponseBody
    public String get()throws JsonProcessingException{
        String json;
        List<OjQuestion>list=ojQuestionRepository.findAll();
        List<OjQuestionShow>res=new ArrayList<OjQuestionShow>();
        for(OjQuestion ojQuestion:list){
            OjQuestionShow show=new OjQuestionShow();
            show.setId(ojQuestion.getId());
            show.setTitle(ojQuestion.getTitle());
            show.setAcceptance(ojQuestion.getAcceptance()+"%");
            show.setDifficulty(ojQuestion.getDifficulty());
            res.add(show);
        }
        json=objectMapper.writeValueAsString(res);
        return json;
    }


}