package com.cokung.comon.utils;

import com.cokung.comon.dto.MemberDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    @Autowired
    private RedisUtil redisUtil;

    public final static long ACCESS_TOKEN_VALIDATION_SECOND = 1000L * 10;
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 24 * 2;

    final static public String ACCESS_TOKEN_NAME = "accessToken";
    final static public String REFRESH_TOKEN_NAME = "refreshToken";

    @Value("${jwt.secret}")
    private String secretKey;

    public Key getSigningKey(String secretKey) {
        byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public String generateAccessToken(String id) {
        Claims claims = Jwts.claims().setSubject(ACCESS_TOKEN_NAME);
        claims.put("id", id);
        String accessToken = doGenerateToken(claims, ACCESS_TOKEN_VALIDATION_SECOND);
        return accessToken;
    }

    public String generateRefreshToken(String id) {
        log.info("Provider ::: refreshToken 발급합니다잉...");
        Claims claims = Jwts.claims().setSubject(REFRESH_TOKEN_NAME);
        String refreshToken = doGenerateToken(claims, REFRESH_TOKEN_VALIDATION_SECOND);
        redisUtil.setData(refreshToken, id);
        return refreshToken;
    }

    public String doGenerateToken(Claims claims, Long expireTime) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireTime))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    public boolean isTokenExpired(String token) {
        Date tokenExpireTime = extractAllClaims(token).getExpiration();
        Date now = new Date();
        return tokenExpireTime.before(now);
    }

    public Claims extractAllClaims(String token) throws ExpiredJwtException {
        log.info("Provider ::: " + token.toString());
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserId(String token) {
        String id = extractAllClaims(token).get("id", String.class);
        log.info("Provider ::: " + id);
        return id;
    }

    public boolean isValidAccessToken(String accessToken, UserDetails userDetails) {
        return getUserId(accessToken).equals(userDetails.getUsername()) && !isTokenExpired(accessToken);
    }

    public boolean isValidRefreshToken(String refreshToken, String userId, String refreshTokenUserId) {
        return userId.equals(getUserId(refreshTokenUserId)) && isTokenExpired(refreshToken);
    }

    public void setSecurityAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
