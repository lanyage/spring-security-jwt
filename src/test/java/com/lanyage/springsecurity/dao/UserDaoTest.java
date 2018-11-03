package com.lanyage.springsecurity.dao;

import com.lanyage.springsecurity.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {
    @Test
    public void saveUser() throws Exception {
        User user = new User();
        user.setUsername("tom");
        user.setPassword(new BCryptPasswordEncoder().encode("tom"));
        Integer insertCount = userDao.saveUser(user);
        System.out.println(insertCount);
    }

    @Autowired
    private UserDao userDao;
    @Test
    public void findUserByUsername() throws Exception {
        User user = userDao.findUserByUsername("lanyage");
        System.out.println(user);
        System.out.println(user.getRoles());
    }
}