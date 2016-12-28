package com.mapper;

import com.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by takahiro on 2016/12/28.
 */
public interface PersonRepository extends JpaRepository<Person,Long>{

}
