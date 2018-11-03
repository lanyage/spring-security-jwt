package com.lanyage.springsecurity.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 任何需要验证的请求都需要调用该类的方法
 * <p>
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。如果校验通过，就认为这是一个取得授权的合法请求。
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Lanyage ")) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken;
        try {
            authenticationToken = getAuthentication(request);
            if(authenticationToken == null) {
                logger.info(">>>>>>Not Authorized>>>>>>");
                response.setStatus(403);
                chain.doFilter(request,response);
                return;
            }
        }catch (SignatureException e) {
            logger.info(">>>>>>SignatureException Threw>>>>>>");
            response.setStatus(403);
            chain.doFilter(request, response);
            return;
        }catch (ExpiredJwtException e) {
            logger.info(">>>>>>ExpiredJwtException Threw>>>>>>");
            response.setContentType("application/json");
            response.getWriter().write("{\"code\":-1, \"msg\":\"expired token\"}");
            response.getWriter().flush();
            return;
        }
        //每次请求都要保存用户的新状态，要不然用户的authentication会丢失
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws SignatureException, ExpiredJwtException {
        String token = request.getHeader("Authorization");
        if (token != null) {
            //解析token
            String user = Jwts.parser()
                        .setSigningKey("MyJwtSecret")
                        .parseClaimsJws(token.replace("Lanyage ", ""))
                        .getBody()
                        .getSubject();

            logger.info("user:" + user);
            if (user != null) {
                //lanyage-[ROLE_ADMIN, ROLE_USER]
                String roles = user.split("-")[1];
                roles = roles.substring(1, roles.length() - 1);

                String[] authorities = roles.split(",");
                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                for (String roleName : authorities) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(roleName.trim()));
                }
                logger.info(grantedAuthorities.toString());
                return new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities);
            }
            return null;
        }
        return null;
    }

    public static void main(String[] args) {
        Claims claims = Jwts.parser()
                .setSigningKey("MyJwtSecret")
                .parseClaimsJws("Lanyage eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI5NmY1ODdmOS1jNDZkLTQ0NWMtOWZkZi01ODViOGIxZDY4OTciLCJzdWIiOiJsYW55YWdlLVtST0xFX0FETUlOLCBST0xFX1VTRVJdIiwiZXhwIjoxNTQxMjIyODc4LCJpc3MiOiJsYW55YWdlIn0.iSEA7GvlMosnNhCg7PhMnvt-86wW73_n-v3jojVqprZwwPcPxIb1e1OwPD4KtdJyX0g9O22O87aQCu5LvemYXw".replace("Lanyage ", ""))
                .getBody();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        claims.setExpiration(new Date(System.currentTimeMillis()));

        Date exp = claims.getExpiration();
//        String subject = claims.getSubject();

        System.out.println(sdf.format(new Date(System.currentTimeMillis())));
        System.out.println(sdf.format(exp));


//        System.out.println(subject);

    }
}
