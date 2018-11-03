基于JWT(json web token)的前后端分离的登录验证


```
<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt</artifactId>
			<version>0.7.0</version>
</dependency>
```

将信息封装到token里

```
String token = Jwts.builder().setSubject("Hello World").signWith(SignatureAlgorithm.HS512, "LANYAGE").compact();
System.out.println(token);
```        

结果是：	`eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJIZWxsbyBXb3JsZCJ9.TYUbEvchKBy7-VmUQLvDeKvJSGUbDuVcMqKO1t3ce90_njX5W97YgMftDRDvgFZspTFTtVu_fCZ35dgurbHiUw`

将token还原:

```
String origin = Jwts.parser().setSigningKey("LANYAGE").parseClaimsJws(token).getBody().getSubject();
System.out.println(origin);
```      
结果是：
`Hello World`  

原理是让客户端自己保存自身的状态，如果没授权，那么就路由到登录页面，那么就返回登录页面就是。

这里还有JWTUtils,用于生成token,判断token是否过期，判断token是不是在用户更新密码之后创建的。
