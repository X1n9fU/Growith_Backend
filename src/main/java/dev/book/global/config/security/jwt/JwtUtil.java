package dev.book.global.config.security.jwt;

import dev.book.global.config.security.dto.TokenDto;
import dev.book.global.config.security.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * 토큰을 제작하고 validate 검증하는 로직
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${spring.jwt.expiration.access}")
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${spring.jwt.expiration.refresh}")
    private long REFRESH_TOKEN_EXPIRATION;
    private Key SECRET_KEY;

    private final RefreshTokenService refreshTokenService;

    @PostConstruct
    protected void init() {
        SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * access + refresh 토큰 생성
     * @param authentication
     * @return
     */
    public TokenDto generateToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .reduce((auth1, auth2) -> auth1 + "," + auth2)
                .orElse("");

        String accessToken = generateAccessToken(authorities, authentication.getName());
        String refreshToken = generateRefreshToken(authorities, authentication.getName());

        //refreshToken 저장
        refreshTokenService.saveRefreshToken(authentication.getName(), refreshToken);

        return new TokenDto(accessToken, refreshToken);

    }

    public String generateAccessToken(String authorities, String authName){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(authName)
                .claim("auth", authorities)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION * 1000))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String authorities, String authName){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(authName)
                .claim("auth", authorities)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION * 1000))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 정상적인 토큰이라면 Subject 반환
     * @param token
     * @return user의 email
     */
    public String validateToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

}
