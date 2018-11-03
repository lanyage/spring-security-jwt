package com.lanyage.springsecurity.dao;

import com.lanyage.springsecurity.domain.User;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lanyage on 2018/10/29.
 */
public interface UserDao {
    User findUserByUsername(String username);

    Integer saveUser(@Param("user") User user);
}
