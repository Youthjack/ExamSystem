package com.controller;

import com.model.Paper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by takahiro on 2016/12/20.
 */
@Controller
@RequestMapping("/practise")
public class PractiseController {

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    @ResponseBody
    public String index(){
        return "index";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        int paper_id=5;
        List<Integer>question_id=new ArrayList<Integer>();
        question_id.add(1);
        question_id.add(3);
        return "true";
    }

    @RequestMapping(value = "/putSession",method = RequestMethod.GET)
    @ResponseBody
    public String putSession(HttpServletRequest request){
        System.out.println(request.getContextPath());
        request.getSession().setAttribute("test","12345");
        return "test";
    }
    @RequestMapping(value = "/getSession",method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpServletRequest request){
        System.out.println(request.getContextPath());
        if(request.getSession().getAttribute("test")!=null)
            return "success";
        else
            return "false;";
    }

}
