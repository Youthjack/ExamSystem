package com.mapper;

import com.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by takahiro on 2016/12/22.
 */
public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question>findAll();

    @Modifying
    @Transactional
    @Query("update Question set question=:ques,answer=:ans,point=:point,chapter=:session where id=:id")
    int updateQuestion(@Param("ques")String ques,@Param("ans")String ans,@Param("point")int point,@Param("session")String session,
                       @Param("id")int id);

    @Modifying
    @Transactional
    @Query("delete from Question where id=:id")
    void deleteQuestionById(@Param("id")int id);

    /*@Modifying
    @Query("insert into Question(ques,ans,point,session,type)values(:ques,:ans,:point,:session,:type)")
    int insertIntoQuestion(@Param("ques")String ques,@Param("ans")String ans,@Param("point")int point,@Param("session")
            String session,@Param("type")int type);*/
}
