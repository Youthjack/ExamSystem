package com.mapper;

import com.model.OjQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by takahiro on 2016/12/27.
 */
public interface OjQuestionRepository extends JpaRepository<OjQuestion,Long>{
    List<OjQuestion>findAll();
}
