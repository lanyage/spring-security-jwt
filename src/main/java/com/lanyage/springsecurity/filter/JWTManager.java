package com.lanyage.springsecurity.filter;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;

public class JWTManager {
    private static final String MY_SECRET = "MyJwtSecret";
    private static final int EXPIRE_TIME_MINUTE_GAP = 1;
//    private static final int EXPIRE_TIME_MINUTE_GAP = 60 * 24;

    public static String create(String authString) {

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, EXPIRE_TIME_MINUTE_GAP);
        Date expireDate = now.getTime();

        String token = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(authString)
                .setExpiration(expireDate)
                .setIssuer(authString.split("-")[0])
                .signWith(SignatureAlgorithm.HS512, MY_SECRET)
                .compact();
        return token;
    }

}
