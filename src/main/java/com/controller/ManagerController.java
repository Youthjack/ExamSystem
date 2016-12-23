package com.controller;

import com.annotation.Authorization;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonModel.Message;
import com.jsonModel.ToManagerSee;
import com.mapper.UserRepository;
import com.model.User;
import org.apache.tomcat.util.digester.ArrayStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
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
        List<User>list=userRepository.findPartAll();
        List<ToManagerSee>res=new ArrayList<ToManagerSee>();
        for(int i=0;i<list.size();i++){
            User user=list.get(i);
            ToManagerSee obj=new ToManagerSee();
            obj.setId(user.getId());
            obj.setUsername(user.getUsername());
            if(user.getIdentity()==1){
                obj.setIdentity("学生");
            }else if(user.getIdentity()==2){
                obj.setIdentity("老师");
            }else if(user.getIdentity()==3){
                obj.setIdentity("管理员");
            }else{
                obj.setIdentity("不明身份者");
            }
            res.add(obj);
        }
        json=objectMapper.writeValueAsString(res);
        return json;
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String add(@RequestBody String body)throws JsonProcessingException{
        Message message=new Message();
        String json;
        User user;
        try {
            user = objectMapper.readValue(body, User.class);
        }catch (IOException e){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(user.getUsername()==null||user.getPassword()==null||user.getIdentity()==0) {
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        try{
            userRepository.save(user);
            message.setStatus("success");
        }catch (Exception e){
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public String update(@RequestBody String body)throws JsonProcessingException{
        Message message=new Message();
        String json;
        User user;
        try {
            user = objectMapper.readValue(body, User.class);
        }catch (IOException e){
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        if(user.getUsername()==null||user.getPassword()==null||user.getIdentity()==0) {
            message.setStatus("error");
            json=objectMapper.writeValueAsString(message);
            return json;
        }
        try{
            userRepository.updateUser(user.getUsername(),user.getPassword(),user.getIdentity());
            message.setStatus("success");
        }catch (Exception e){
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
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
            userRepository.deleteUserByUsername(user.getUsername());
            message.setStatus("success");
        }catch (Exception e){
            message.setStatus("error");
        }
        json=objectMapper.writeValueAsString(message);
        return json;
    }
}
