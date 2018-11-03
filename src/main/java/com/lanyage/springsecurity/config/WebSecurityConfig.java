package com.lanyage.springsecurity.config;

import com.lanyage.springsecurity.filter.JWTAuthenticationFilter;
import com.lanyage.springsecurity.filter.JWTLoginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info(">>>>>>" + authenticationManager() + ">>>>>>" + authenticationManager().hashCode());
        http.cors().and().csrf().disable().authorizeRequests()
                //.antMatchers("/user/hello").permitAll()
                //.antMatchers("/user/**").access("hasRole('ROLE_HELLO')")
                .antMatchers(HttpMethod.POST, "/user/login").permitAll()    //所有人可以登录
                .antMatchers(HttpMethod.POST, "/user/signup").permitAll()   //所有人可以请求/user/signup
                .anyRequest().authenticated()           //其他所有的请求都要校验
                .and()
                .addFilter(new JWTLoginFilter(authenticationManager())) //添加拦截器
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .logout().logoutUrl("/logout").permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        logger.info(">>>>>> configure authenticationProvider>>>>>>");
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
