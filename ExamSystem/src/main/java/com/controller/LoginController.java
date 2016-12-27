package com.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapper.UserRepository;
import com.model.User;
import com.util.LoginAuthModel;
import com.util.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by takahiro on 2016/12/20.
 */
@Controller
public class LoginController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RedisTemplate redisTemplate;

    ObjectMapper objectMapper=new ObjectMapper();

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        if(redisTemplate!=null)
            System.out.println("redistemplate不为空");
        else
            System.out.println("redistemplate为空");
        return "Login";
    }

    /*
    @RequestMapping(value = "/login_auth",method = RequestMethod.POST)
    @ResponseBody
    public String login_auth(@RequestParam("username")String username,@RequestParam("password")String password,
                             @RequestParam("identity")int identity){
        return username+" "+password+" "+identity;
    }*/
    @RequestMapping(value = "/login_auth",method = RequestMethod.POST)
    @ResponseBody
    public String login_auth(@RequestBody String body, HttpServletRequest request)throws JsonProcessingException{
        System.out.println(body);
        LoginAuthModel obj;
        Token token=new Token();
        String json=new String();
        try {
            obj = objectMapper.readValue(body, LoginAuthModel.class);
            System.out.println("成功解析上传的json");
        }catch (IOException e){
            token.setMessage("fail");
            json=objectMapper.writeValueAsString(token);
            System.out.println("不能解析上传的json");
            return json;
        }
        String username=obj.getUsername();
        String password=obj.getPassword();
        int identity=obj.getIdentity();
        System.out.println(username+" "+password+" "+identity);
        if(identity==1){

        }else if(identity==2){

        }else if(identity==3){
            //对用户名和密码的处理
            User manager=userRepository.findByUsernameAndPassword(username,password);
            if(manager!=null){
                token.setMessage("success");
                token.setToken(UUID.randomUUID().toString().replace("-",""));
                token.setUserId(manager.getId());
                //redisTemplate.boundValueOps(token.getUserId()).set(token.getToken(),10, TimeUnit.MINUTES);
                request.getSession().setAttribute(token.getToken(),"true");
            }else{
                System.out.println("用户名或密码错误");
                token.setMessage("fail");
            }
            json = objectMapper.writeValueAsString(token);
            return json;
        }
        token.setMessage("fail");
        json=objectMapper.writeValueAsString(token);
        return json;
    }

}
