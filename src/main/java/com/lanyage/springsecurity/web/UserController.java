package com.lanyage.springsecurity.web;

import com.lanyage.springsecurity.domain.User;
import com.lanyage.springsecurity.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Secured(value = "ROLE_ADMIN")
    @GetMapping("/hello")
    public String hello() {
        logger.info("authentication " + SecurityContextHolder.getContext().getAuthentication());
        return "hello";
    }

    @PostMapping("/signup")
    public Object signup(@RequestBody User user) {
        try {
            Assert.notNull(user.getUsername(),"username should not be null");
            Assert.notNull(user.getPassword(),"username should not be null");
        }catch (Exception e) {
            return e.getMessage();
        }
        System.out.println(user);
        userService.save(user);
        return "save successfully";
    }

    @PostMapping("/login")
    public String login() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return "login";
    }
}
