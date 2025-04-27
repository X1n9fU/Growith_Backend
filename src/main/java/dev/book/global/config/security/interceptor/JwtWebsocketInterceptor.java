package dev.book.global.config.security.interceptor;

import dev.book.global.config.security.jwt.JwtAuthenticationToken;
import dev.book.global.config.security.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtWebsocketInterceptor implements HandshakeInterceptor {

    private final static String ACCESS_TOKEN = "access_token";
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // 웹소켓연결전 실행되는 메소드
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletServerHttpRequest) {

            try {
                // 처음에 오는 ServerHttpRequest는 추상화 된 인터페이스라 다운 캐스팅이 필요함
                HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();
                // httpServletRequest 에서 쿠키를 꺼내 토큰을 추출한다.
                //Map<String, Object> attributes : 각 websocket 세션은 attributes에 대한 Map 을 가지고있다 그 맵에 추출한 토큰을 저장한다.
                Cookie[] cookies = httpServletRequest.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals(ACCESS_TOKEN)) {
                            String token = cookie.getValue();

                            String email = jwtUtil.validateToken(token);
                            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                            if (userDetails != null) {
                                SecurityContextHolder.getContext().setAuthentication(
                                        new JwtAuthenticationToken(userDetails, userDetails.getAuthorities()));
                                attributes.put(ACCESS_TOKEN, token);
                                return true;
                            } else SecurityContextHolder.clearContext();

                        }
                    }
                }
            } catch (ExpiredJwtException e) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                log.error("만료된 JWT 토큰입니다. :{}", e.getMessage());

            } catch (MalformedJwtException | JwtException | IllegalArgumentException e) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                log.error("잘못된 JWT 토큰입니다. :{}", e.getMessage());
            } catch (Exception e) {
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                log.error("서버 오류 : {}" , e.getMessage());
            }

        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
