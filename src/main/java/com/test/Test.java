package com.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.util.LoginAuthModel;

/**
 * Created by takahiro on 2016/12/20.
 */
public class Test {
    public static void main(String args[])throws JsonProcessingException{
        ObjectMapper objectMapper=new ObjectMapper();
        LoginAuthModel model=new LoginAuthModel();
        model.setIdentity(1);
        model.setUsername("takahiro");
        model.setPassword("1234");
        String json=objectMapper.writeValueAsString(model);
        System.out.println(json);

    }
}
