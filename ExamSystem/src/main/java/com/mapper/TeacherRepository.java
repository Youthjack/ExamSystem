package com.mapper;

import com.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by takahiro on 2016/12/23.
 */
public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    List<Teacher>findAll();
    Teacher findById(int id);
    Teacher findByNumber(String number);


    @Modifying
    @Transactional
    @Query("delete from Teacher where number=:number")
    int deleteTeacherByNumber(@Param("number")String number);

    @Modifying
    @Transactional
    @Query("update Teacher set name=:name,email=:email where number=:number")
    int updateTeacherByNumber(@Param("name")String name,@Param("email")String email,@Param("number")String number);
}
