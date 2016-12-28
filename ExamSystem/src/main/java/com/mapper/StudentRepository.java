package com.mapper;

import com.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by takahiro on 2016/12/23.
 */
public interface StudentRepository extends JpaRepository<Student,Long>{
    List<Student>findAll();
    Student findById(int id);
    Student findByNumber(String number);
    List<Student>findByClassName(String className);

    @Modifying
    @Query("delete from Student where number=:number")
    @Transactional
    int deleteStudentByNumber(@Param("number")String number);

    @Modifying
    @Query("update Student set name=:name,className=:className where number=:number")
    @Transactional
    int updateStudentByNumber(@Param("name")String name,@Param("className")String className,@Param("number")String number);
}
