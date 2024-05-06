package com.eunsun.storereservation.security;

import com.eunsun.storereservation.exception.UserEmailNotFoundException;
import com.eunsun.storereservation.service.CustomerService;
import com.eunsun.storereservation.service.ManagerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1H

    private static final String KEY_ROLES = "role";

    private final CustomerService customerService;
    private final ManagerService managerService;

    @Value("${jwt.secret.key}")
    private String secretKey;

    // secretKey : String -> byte
    private Key getSignKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public String generateToken(String username, String roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles);

        var now = new Date();
        var expireDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSignKey(secretKey), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getEmail(String token) {
        return this.parseClaims(token).getSubject();
    }

    // 사용자의 인증 정보
    public Authentication getAuthentication(String jwt) {
        String email = this.getEmail(jwt);
        UserDetails userDetails = null;

        try {
            userDetails = loadManagerByEmail(email);
            log.info("Manager 인증 정보로 로그인");
        } catch (UsernameNotFoundException e) {
            log.warn("Manager 인증 정보를 찾을 수 없습니다. Customer 인증 정보를 확인합니다.");
        }

        if (userDetails == null) {
            try {
                userDetails = loadCustomerByEmail(email);
                log.info("Customer 인증 정보로 로그인");
            } catch (UsernameNotFoundException e) {
                log.warn("Customer 인증 정보를 찾을 수 없습니다.");
                throw new UsernameNotFoundException("사용자 인증 정보를 찾을 수 없습니다.");
            }
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private UserDetails loadManagerByEmail(String email) {
        try {
            return managerService.loadUserByEmail(email);
        } catch (UserEmailNotFoundException e) {
            log.warn("Manager DB에서 이메일(아이디)을 가진 사용자를 찾을 수 없습니다 -> " + email);
            throw new UsernameNotFoundException("Manager not found with email: " + email);
        }
    }

    private UserDetails loadCustomerByEmail(String email) {
        try {
            return customerService.loadUserByEmail(email);
        } catch (UserEmailNotFoundException e) {
            log.warn("Customer DB에서 이메일(아이디)을 가진 사용자를 찾을 수 없습니다 -> " + email);
            throw new UsernameNotFoundException("Customer not found with email: " + email);
        }
    }



    // 토큰 파싱 -> claim 추출
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey(secretKey))
                    .build()
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }


    // Authentication 객체 -> 로그인한 매니저의 ID
    public Long managerIdFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        // UserDetails 추출
        Object principal = authentication.getPrincipal();
        if (principal instanceof User userDetails) {
            String managerEmail = userDetails.getUsername();
            UserDetails managerDetails = loadManagerByEmail(managerEmail);
            if (managerDetails != null) {
                return managerService.findManagerIdByEmail(managerEmail);
            }
        }

        return null;
    }

    // 로그인한 customer ID
    public Long customerIdFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User userDetails) {
            String customerEmail = userDetails.getUsername();
            UserDetails customerDetails = loadCustomerByEmail(customerEmail);
            if (customerDetails != null) {
                return customerService.findCustomerIdByEmail(customerEmail);
            }
        }

        return null;
    }

}

