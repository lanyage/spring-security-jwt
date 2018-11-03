//package com.lanyage.springsecurity.util;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.io.Serializable;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class JwtTokenUtils implements Serializable {
//
//    private static final long serialVersionUID = -3301605591108950415L;
//
//    private static final String CLAIM_KEY_USERNAME = "sub";
//    private static final String CLAIM_KEY_CREATED = "created";
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private Long expiration;
//
//    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());  //存储用户名
//        claims.put(CLAIM_KEY_CREATED, new Date());      //存储创建时间
//        return generateToken(claims);
//    }
//
//    String generateToken(Map<String, Object> claims) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setExpiration(generateExpirationDate())
//                .signWith(SignatureAlgorithm.HS512, "Lanyage")
//                .compact();
//    }
//
//    public String getUsernameFromToken(String token) {
//        String username;
//        try {
//            final Claims claims = getClaimsFromToken(token);
//            username = claims.getSubject();
//        } catch (Exception e) {
//            username = null;
//        }
//        return username;
//    }
//
//    public static void main(String[] args) {
//        JwtTokenUtils jwtTokenUtils = new JwtTokenUtils();
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("sub","lanyage");
//        claims.put("created", new Date());
//        String token = jwtTokenUtils.generateToken(claims);
//
//        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsYW55YWdlIiwiY3JlYXRlZCI6MTU0MTE3MzM2NzMyOCwiZXhwIjoxNTQxMTczOTY3fQ.-tpcXapHhFxLRJn3bQhcyujx_1DZJgW3qMLQS4-Xqjg--UGF-G9IR8i0lYKtPrwY3CWj93nLaDxXMfESLfONgQ";
//        System.out.println(token);
//
//        System.out.println(jwtTokenUtils.getCreatedDateFromToken(token));
//
//        System.out.println(jwtTokenUtils.isTokenExpired(token));
//    }
//
//    public Date getCreatedDateFromToken(String token) {
//        Date created;
//        try {
//            final Claims claims = getClaimsFromToken(token);
//            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
//        } catch (Exception e) {
//            created = null;
//        }
//        return created;
//    }
//
//    public Date getExpirationDateFromToken(String token) {
//        Date expiration;
//        try {
//            final Claims claims = getClaimsFromToken(token);
//            expiration = claims.getExpiration();
//        } catch (Exception e) {
//            expiration = null;
//        }
//        return expiration;
//    }
//
//    public String refreshToken(String token) {
//        String refreshedToken;
//        try {
//            final Claims claims = getClaimsFromToken(token);
//            claims.put(CLAIM_KEY_CREATED, new Date());
//            refreshedToken = generateToken(claims);
//        } catch (Exception e) {
//            refreshedToken = null;
//        }
//        return refreshedToken;
//    }
//
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        JwtUser user = (JwtUser) userDetails;
//        final String username = getUsernameFromToken(token);    //获取用户名
//        final Date created = getCreatedDateFromToken(token);    //获取token创建时间
//        //final Date expiration = getExpirationDateFromToken(token);
//        return (
//                username.equals(user.getUsername()) //用户名和内存中的authentication对得上
//                        && !isTokenExpired(token)   //token还没过期
//                        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));    //上一次密码修改之后创建的token，也就是token是全新的
//    }
//
//    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
//        final Date created = getCreatedDateFromToken(token);    //获取创建时间
//        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)    //如果
//                && !isTokenExpired(token);
//    }
//
//    private Boolean isTokenExpired(String token) {
//        final Date expiration = getExpirationDateFromToken(token);
//        return null == expiration || expiration.before(new Date());
//    }
//
//    private Claims getClaimsFromToken(String token) {
//        Claims claims;
//        try {
//            claims = Jwts.parser()
//                    .setSigningKey("Lanyage")
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (Exception e) {
//            claims = null;
//        }
//        return claims;
//    }
//
//    private Date generateExpirationDate() {
//        return new Date(System.currentTimeMillis() + 600 * 1000);
//    }
//    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
//        return (lastPasswordReset != null && created.before(lastPasswordReset));
//    }
//}