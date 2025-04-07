//package com.example.demo.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import io.jsonwebtoken.security.SignatureException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//
//@Component
//public class JwtTokenProvider {
//
//    @Value("${app.jwtSecret:JWTSuperSecretKey}")
//    private String jwtSecret;
//
//    @Value("${app.jwtExpirationInMs:86400000}")
//    private int jwtExpirationInMs;
//
//    public String generateToken(Authentication authentication) {
//        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
//
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
//
//        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
//
//        return Jwts.builder()
//                .setSubject(userPrincipal.getId())
//                .claim("username", userPrincipal.getUsername())
//                .claim("email", userPrincipal.getEmail())
//                .claim("role", userPrincipal.getAuthorities().iterator().next().getAuthority())
//                .setIssuedAt(new Date())
//                .setExpiration(expiryDate)
//                .signWith(key)
//                .compact();
//    }
//
//    public String getUserIdFromJWT(String token) {
//        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
//
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        return claims.getSubject();
//    }
//
//    public String getRoleFromJWT(String token) {
//        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
//
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        return claims.get("role", String.class);
//    }
//
//    public boolean validateToken(String authToken) {
//        try {
//            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
//            return true;
//        } catch (SignatureException ex) {
//            System.out.println("Invalid JWT signature");
//        } catch (MalformedJwtException ex) {
//            System.out.println("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            System.out.println("Expired JWT token");
//        } catch (UnsupportedJwtException ex) {
//            System.out.println("Unsupported JWT token");
//        } catch (IllegalArgumentException ex) {
//            System.out.println("JWT claims string is empty");
//        }
//        return false;
//    }
//}
