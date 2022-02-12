package com.cokung.comon.util;

import com.cokung.comon.dto.MemberDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    public final static long TOKEN_VALIDATION_SECOND = 1000L * 60 *10 ; // 유효시간 10분
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 60 * 24 * 2; //유효시간 2일

    final static public String ACCESS_TOKEN_NAME = "accessToken";
    final static public String REFRESH_TOKEN_NAME = "refreshToken";

//    private static String SECRET_KEY="C0DA2F1C08FCF502F64DC11A4F7A28134F5EE6B2A45318279F557CD32B4A59FA";
    private static String SECRET_KEY;

    @Value("${jwt.secret}")
    private void setSecretKey(String SECRET_KEY){
        this.SECRET_KEY = SECRET_KEY;
        System.out.println("비밀키"+this.SECRET_KEY);
    }

    // 서명된 비밀키 리턴
    private Key getSigningKey(String secretKey) {
        byte[] keyBytes= secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //토큰 유효 검사 후, 토큰 payload 값 가져옴
    public Claims extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 전달 후 유저 정보 받아오기
    public String getUserId(String token) {
        return extractAllClaims(token).get("userId", String.class);
    }

    // 토큰 만료되었는지 검사
    public Boolean isTokenExpired(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // 토큰 생성 -> doGenerateToken
    public String generateAccessToken(String id) {
        return doGenerateToken(id, TOKEN_VALIDATION_SECOND);
    }

    // refresh token 생성
    public String generateRefreshToken(String id) {
        return doGenerateToken(id, REFRESH_TOKEN_VALIDATION_SECOND);
    }

    // access token 생성
    public String doGenerateToken(String userId, long expireTime) {

        Claims claims = Jwts.claims();
        claims.put("userId", userId); //payload에 담을 정보

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    // token 유효성 검사
    public Boolean validateToken(String token, MemberDto memberDto) {
        /*token payload의 userId와 memberDto의 id가 일치하는지
        * 유효기간이 만료되지않은 토큰인지 검사*/
        final String userId = getUserId(token);

        return (userId.equals(memberDto.getId()) && !isTokenExpired(token));
    }


}
