package com.mapper;

import com.model.Paper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by takahiro on 2016/12/23.
 */
public interface PaperRepository extends JpaRepository<Paper,Long>{
    List<Paper>findAll();

    Paper findById(int id);

    @Modifying
    @Query("delete from Paper where id=:id")
    @Transactional
    int deletePaperById(@Param("id")int id);

//    @Query("select a from Paper a,Question b,Paper_Question c where a.id=c.paperId and b.id=c.questionId")
//    List<Paper>findAllPaperQuestion();
}
