package com.mapper;

import com.model.Problem;
import com.model.ProblemPk;
import com.model.StudentPaperPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Jack on 2016/12/27.
 */
public interface ProblemRepository extends JpaRepository<Problem,ProblemPk> {
    List<Problem> findByPkStudentId(int studentId);
    Problem findByPk(ProblemPk pk);
}
