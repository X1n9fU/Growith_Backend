package dev.book.global.config.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 헤더에서 토큰을 꺼내서 유효성 검증
 * 존재하지 않음 , 토큰 만료, 올바르지 않은 토큰에 대하여 에러 처리
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        try {
            String email = jwtUtil.validateToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (userDetails != null)
                SecurityContextHolder.getContext().setAuthentication(
                    new JwtAuthenticationToken(userDetails, userDetails.getAuthorities()));
            else SecurityContextHolder.clearContext();

        } catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            logger.error("만료된 JWT 토큰입니다. : " + e.getMessage());
        } catch (JwtException | IllegalArgumentException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            logger.error("잘못된 JWT 토큰입니다." + e.getMessage());
        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("서버 오류" + e.getMessage());
        }

        filterChain.doFilter(request ,response);
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer "))
            return null;
        return header.substring(7);
    }
}
