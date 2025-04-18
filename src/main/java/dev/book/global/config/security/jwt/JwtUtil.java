package dev.book.global.config.security.jwt;

import dev.book.global.config.security.dto.TokenDto;
import dev.book.global.config.security.service.refresh.RefreshTokenService;
import dev.book.global.config.security.util.CookieUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

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
    private final String ACCESS_TOKEN = "access_token";
    private final String REFRESH_TOKEN = "refresh_token";
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
    public TokenDto generateToken(HttpServletResponse response, Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = generateAccessToken(authorities, authentication.getName());
        String refreshToken = generateRefreshToken(authorities, authentication.getName());

        //refreshToken 저장
        refreshTokenService.saveAndUpdateRefreshToken(authentication.getName(), refreshToken);

        TokenDto tokenDto = new TokenDto(accessToken, refreshToken);
        addTokenInCookie(response, tokenDto);

        return tokenDto;
    }

    public void generateAccessToken(HttpServletResponse response, Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = generateAccessToken(authorities, authentication.getName());

        addAccessTokenInCookie(response, accessToken);
    }

    private String generateAccessToken(String authorities, String authName){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(authName)
                .claim("auth", authorities)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION * 1000))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(String authorities, String authName){
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

    public String getRefreshToken(HttpServletRequest request){
        return CookieUtil.getCookie(request, REFRESH_TOKEN);
    }

    public void addTokenInCookie(HttpServletResponse response, TokenDto tokenDto) {
        CookieUtil.addCookie(response, ACCESS_TOKEN, tokenDto.accessToken(), (int) ACCESS_TOKEN_EXPIRATION * 1000);
        CookieUtil.addCookie(response, REFRESH_TOKEN, tokenDto.refreshToken(), (int) REFRESH_TOKEN_EXPIRATION * 1000);
    }

    public void addAccessTokenInCookie(HttpServletResponse response, String accessToken){
        CookieUtil.addCookie(response, ACCESS_TOKEN, accessToken, (int) ACCESS_TOKEN_EXPIRATION * 1000);
    }

    public void deleteAccessTokenAndRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN);
    }
}

