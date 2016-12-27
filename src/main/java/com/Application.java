package com;

import com.interceptor.AuthorizationInterceptor;
import com.mapper.StudentRepository;
import com.mapper.TeacherRepository;
import com.mapper.UserRepository;
import com.model.Student;
import com.model.Teacher;
import com.model.User;
import com.model.basicEnum.IdentifyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by takahiro on 2016/12/20.
 */
//@Configuration
//@ComponentScan
//@EnableAutoConfiguration
//@EnableTransactionManagement
@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter implements CommandLineRunner{
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
                configurableEmbeddedServletContainer.setSessionTimeout(10,TimeUnit.MINUTES);
                ErrorPage error401Page=new ErrorPage(HttpStatus.UNAUTHORIZED,"/401.html");
                ErrorPage error404Page=new ErrorPage(HttpStatus.NOT_FOUND,"/404.html");
                ErrorPage error500Page=new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/500.html");

                configurableEmbeddedServletContainer.addErrorPages(error401Page,error404Page,error500Page);
            }
        };
    }

    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new AuthorizationInterceptor()).addPathPatterns("/manager/**");
    }
    public static void main(String  args[]){
        SpringApplication.run(Application.class,args);
    }
    @Override
    public void run(String... strings) throws Exception {
        if(userRepository.findByUsername("admin") != null) {
            return;
        }
        User u1 = new User("233", "123456", IdentifyType.STUDENT);
        User u2 = new User("666", "123456", IdentifyType.TEACHER);
        User u3 = new User("admin", "123456", IdentifyType.ADMIN);
        Student student = new Student();
        student.setNumber(u1.getUsername());
        student.setName("学生");
        student.setClassName("网工");
        Teacher teacher = new Teacher();
        teacher.setNumber(u2.getUsername());
        teacher.setName("老师");
        userRepository.save(u1);
        studentRepository.save(student);
        userRepository.save(u2);
        teacherRepository.save(teacher);
        userRepository.save(u3);
    }
}
