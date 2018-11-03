package com.lanyage.springsecurity.test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
//    public static void main(String[] args) {
//        // secret
//        String secret = "Lanyage";
//
//        // header
//        Map<String, Object> header = new HashMap<>();
//        header.put("alg", "HS512");
//        header.put("typ", "JWT");
//
//        // claims
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("username", "lanyage");
//        claims.put("roles", Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
//        String token = Jwts.builder()
//                .setHeader(header)
//                .setClaims(claims)
//                .setSubject("jwt")
//                .setExpiration(new Date(System.currentTimeMillis() + 600 * 1000))
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//        System.out.println(token);
//    }

    public static void main(String[] args) {
        String secret = "Lanyage";
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqd3QiLCJyb2xlcyI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIl0sImV4cCI6MTU0MTEyNDk5OCwidXNlcm5hbWUiOiJsYW55YWdlIn0.AO4DTC3aC62TWs5rMPfPCIbRm3JdzuJIu-J3uK1mpyp5-i3DtfxaynOgYYWIqZOA_WRglpqrFZ4hMYZ_TZzDHw")
                    .getBody();
        }catch (Exception e) {
            System.out.println(e.getClass());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(format.format(claims.getExpiration()));
    }
}
