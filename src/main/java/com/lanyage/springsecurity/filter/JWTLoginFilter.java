package com.lanyage.springsecurity.filter;

import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.lanyage.springsecurity.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端。/login的时候会调用该类方法进行验证
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    /**
     * 接受并解析用户凭证
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 使用ProviderManager来验证UsernamePasswordAuthenticationToken
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            //验证通过就将数据Authentication存入SecurityContextHolder,否则就抛异常
            return authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    user.getUsername(),
                                    user.getPassword(),
                                    new ArrayList<>()
                            )
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用户登录成功后，这个方法会被调用，我们在这个方法里面生成token
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        logger.info(">>>>>> successfulAuthentication >>>>>>");
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        List<String> roleList = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            roleList.add(authority.getAuthority());
        }
        String authString = authResult.getName() + "-" + roleList;
        String token = JWTManager.create(authString);   //创建token
        logger.info("token : " + token);
        response.addHeader("Authorization", "Lanyage " + token);
    }

}
