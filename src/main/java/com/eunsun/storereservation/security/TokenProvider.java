package com.eunsun.storereservation.security;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import zerobase.dividends.service.MemberService;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
public class TokenProvider {
//
//    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1H
//    private static final String KEY_ROLES = "roles";
//
//    private final MemberService memberService;
//
//    @Value("${jwt.secret.key}")
//    private String secretKey;
//
//    // 토큰 생성(발급)
//    public String generateToken(String username, List<String> roles) {
//        Claims claims = Jwts.claims().setSubject(username);
//        claims.put(KEY_ROLES, roles); // key-value 형태로
//
//        var now = new Date();
//        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now) // 토큰 생성 시간
//                .setExpiration(expiredDate) // 토큰 만료 시간
//                .signWith(getSignKey(secretKey), SignatureAlgorithm.HS512) // 비밀키, 사용화 알고리즘
//                .compact();
//    }
//
//    // secretKey : String -> byte
//    private Key getSignKey(String secretKey) {
//        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public Authentication getAuthentication(String jwt) {
//        UserDetails userDetails = this.memberService.loadUserByUsername(this.getUsername(jwt));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
//
//    public String getUsername(String token) {
//        return this.parseClaims(token).getSubject();
//    }
//
//    public boolean validateToken(String token) {
//        if (!StringUtils.hasText(token)) {
//            return false;
//        }
//        var claims = this.parseClaims(token);
//        return !claims.getExpiration().before(new Date());
//    }
//
//    private Claims parseClaims(String token) {
//        try {
//            return Jwts.parserBuilder()
//                    .setSigningKey(getSignKey(secretKey))
//                    .build()
//                    .parseClaimsJws(token).getBody();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        }
//    }
}
