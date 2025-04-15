package dev.book.global.config.security.controller;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.global.config.security.service.AuthService;
import dev.book.user.dto.request.UserSignUpRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal CustomUserDetails userDetails){
        authService.logout(userDetails);
        return ResponseEntity.ok().build();
    }
}
