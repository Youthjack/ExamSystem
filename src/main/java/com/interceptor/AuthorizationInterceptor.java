package com.interceptor;

import com.annotation.Authorization;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by takahiro on 2016/12/21.
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    /*
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("验证用户开始了...");
        if(!(handler instanceof HandlerMethod)){
            System.out.println("handler不对??");
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method=handlerMethod.getMethod();

        if(method.getAnnotation(Authorization.class)==null) {
            System.out.println("方法没有注解");
            return true;
        }

        header好像不行，用get试下
        String authorization=request.getHeader("authorization");
        if(authorization==null||authorization.length()==0)
            return false;
        String[]param=authorization.split("_");         //注意这里传过来的是以_分割
        if(param.length!=2)
            return false;
        //客户端传进来的header里的附带参数
        int userId=Integer.parseInt(param[0]);
        String token=param[1];
        String token=request.getParameter("data");
        String id=request.getParameter("userId");
        if(token==null||id==null) {
            System.out.println("没有传入参数");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        int userId=Integer.parseInt(id);
        System.out.println("验证过程中:"+token+"  "+userId);

        if(redisTemplate==null)
            System.out.println("redistemplate为空");
        Object obj=redisTemplate.boundValueOps(userId).get();
        if(obj==null){
            System.out.println("不存在这个令牌值");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        String redisMessage=(String)obj;
        if(redisMessage==null||!token.equals(redisMessage)) {
            //request.setAttribute("CURRENT_USER_ID",model.getUserId());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println("没有权限");
            return false;
        }
        System.out.println("验证通过了");
        redisTemplate.boundValueOps(userId).expire(10, TimeUnit.MINUTES);   //延长登录时间
        return true;
    }*/
    @Autowired
    private RedisTemplate<String,String>redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("验证用户开始了...");
        if(redisTemplate!=null)
            System.out.println("redistemplate不为空");
        else
            System.out.println("redistemplate为空");
        if(!(handler instanceof HandlerMethod)){
            System.out.println("handler不对??");
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method=handlerMethod.getMethod();

        if(method.getAnnotation(Authorization.class)==null) {
            System.out.println("方法没有注解");
            return true;
        }
        String token=request.getParameter("data");
        String id=request.getParameter("userId");
        if(token==null||id==null) {
            System.out.println("没有传入参数");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        int userId=Integer.parseInt(id);
        System.out.println("验证过程中:"+token+"  "+userId);

        if(request.getSession().getAttribute(token)!=null) {
            System.out.println("验证通过了");
            request.getSession().setAttribute(token,"true");    //延长session时间
            return true;
        } else {
            System.out.println("验证不通过");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}

