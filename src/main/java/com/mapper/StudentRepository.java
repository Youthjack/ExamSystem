package com.mapper;

import com.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Jack on 2016/12/23.
 */
public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findById(int id);
}
