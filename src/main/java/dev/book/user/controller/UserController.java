package dev.book.user.controller;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.user.dto.request.UserProfileUpdateRequest;
import dev.book.user.dto.response.UserProfileResponse;
import dev.book.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok()
                .body(userService.getUserProfile(userDetails));
    }

    //todo 업적, 통계, 경고 반환

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateUserProfile(@RequestBody UserProfileUpdateRequest profileUpdateRequest,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok()
                .body(userService.updateUserProfile(profileUpdateRequest, userDetails));
    }

    //todo 카테고리 변경

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails){
        userService.deleteUser(userDetails);
        return ResponseEntity.ok()
                .build();
    }
}
