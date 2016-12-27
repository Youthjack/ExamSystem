package com.mapper;

import com.model.Exam;
import com.model.StudentPaperPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by takahiro on 2016/12/26.
 */
public interface ExamRepository extends JpaRepository<Exam,StudentPaperPk> {
    List<Exam>findByPkStudentId(int studentId);
    Exam findByPk(StudentPaperPk pk);
}
