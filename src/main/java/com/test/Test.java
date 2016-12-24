package com.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapper.PaperRepository;
import com.model.Paper;
import com.model.Question;
import com.util.LoginAuthModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by takahiro on 2016/12/20.
 */
public class Test {

    public static void main(String args[])throws JsonProcessingException{
/*
        ObjectMapper objectMapper=new ObjectMapper();
        LoginAuthModel model=new LoginAuthModel();
        model.setIdentity(1);
        model.setUsername("takahiro");
        model.setPassword("1234");
        String json=objectMapper.writeValueAsString(model);
        System.out.println(json);
*/
        ObjectMapper objectMapper=new ObjectMapper();
        LoginAuthModel model=new LoginAuthModel();
        Set<Question> questionSet = new HashSet<Question>();
        for(int i=0;i<26;i++) {
            Question question = new Question();
            question.setQuestion("a"+i);
            questionSet.add(question);
        }
        Paper paper = new Paper("test","",questionSet);
        String json=objectMapper.writeValueAsString(paper);
        System.out.println(json);
    }
}
