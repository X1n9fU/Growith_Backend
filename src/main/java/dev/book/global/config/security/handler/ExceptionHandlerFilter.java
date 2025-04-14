package dev.book.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 예외 처리 필터
 * - 예외 발생 시, 클라이언트에게 응답을 보내는 필터
 */
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("[doFilterInternal] Exception in ExceptionHandlerFilter: ", e);
            sendErrorResponse(response, e.getMessage());
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String msg) throws IOException {
        ResponseEntity<?> responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(msg);

        // Set the HttpServletResponse properties
        response.setStatus(responseEntity.getStatusCode().value());
        response.setContentType("application/json");

        // Use an ObjectMapper to write the ResponseEntity body as JSON to the response output stream
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), responseEntity.getBody());
    }

}