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

    /*如何手动级联更新
    @Modifying
    @Query("update Paper a,Paper_Question b,Question c set b.question_id=:question_id where c.id=:question_id and " +
            "a.id=b.paper_id and a.id=:paper_id")
    int myUpdate(@Param("question_id")int question_id,@Param("paper_id")int paper_id);*/

    /*@Query("select a from Paper a,Question b,Paper_Question c where a.Id=c.paperId and b.Id=c.questionId")
    List<Paper>findAllPaperQuestion();*/
}
