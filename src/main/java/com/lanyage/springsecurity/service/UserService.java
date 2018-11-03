package com.lanyage.springsecurity.service;

import com.lanyage.springsecurity.dao.UserDao;
import com.lanyage.springsecurity.domain.Role;
import com.lanyage.springsecurity.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Ready to load user by username.");
        User user = userDao.findUserByUsername(username);
        if(user != null) {
            List<Role> roles = user.getRoles();
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for(Role role : roles) {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            logger.info("user = " + user);
            logger.info("roles = " + user.getRoles());
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
        }
        throw new UsernameNotFoundException(username);
    }

    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.saveUser(user);
    }
}
