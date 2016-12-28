package com.mapper;

import com.model.OjQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by takahiro on 2016/12/27.
 */
public interface OjQuestionRepository extends JpaRepository<OjQuestion,Long>{
    List<OjQuestion>findAll();
    OjQuestion findById(long id);

    @Modifying
    @Query("update OjQuestion set totalSubmit=totalSubmit+1 where id=:id")
    @Transactional
    int submit(@Param("id")long id);

    @Modifying
    @Query("update OjQuestion set correctSubmit=correctSubmit+1 where id=:id")
    @Transactional
    int ac(@Param("id")long id);

    @Modifying
    @Query("update OjQuestion set acceptance=correctSubmit/totalSubmit where id=:id")
    @Transactional
    int refresh(@Param("id")long id);
}
