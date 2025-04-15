package dev.book.global.config.security.controller;

import dev.book.global.config.security.service.AuthService;
import dev.book.user.dto.UserSignUpRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpRequest userSignupRequest, HttpServletResponse response){
        authService.signUp(userSignupRequest, response);
        return ResponseEntity.ok()
                .build();
    }
}
