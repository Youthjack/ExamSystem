package com.mapper;

import com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by takahiro on 2016/12/21.
 */
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPasswordAndIdentity(String username,String password,int identity);
    User findByUsername(String username);
    User findByUsernameAndPassword(String username,String password);
    List<User> findAll();

    @Query("select id,username,identity from User order by identity,id")
    List<User>findPartAll();

    @Modifying
    @Query("update User set password=:password,identity=:identity where username=:username")
    int updateUser(@Param("username")String username,@Param("password")String password,@Param("identity")String identity);

    @Modifying
    @Query("delete from User where username=:username")
    @Transactional
    void deleteUserByUsername(@Param("username")String username);
}
