package dev.book.global.config.security.controller;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.global.config.security.dto.TokenDto;
import dev.book.global.config.security.service.AuthService;
import dev.book.user.dto.request.UserSignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthSwaggerController{

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpRequest userSignupRequest,
                                    @RequestParam("email") @Email String email, HttpServletResponse response){
        authService.signUp(userSignupRequest, email, response);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response,
                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        authService.logout(request, response, userDetails);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(HttpServletRequest request, HttpServletResponse response){
        authService.reissueToken(request, response);
        return ResponseEntity.ok().build();
    }
}
