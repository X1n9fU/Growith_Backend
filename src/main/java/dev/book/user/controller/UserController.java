package dev.book.user.controller;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.user.dto.request.UserCategoriesRequest;
import dev.book.user.dto.request.UserProfileUpdateRequest;
import dev.book.user.dto.response.UserProfileResponse;
import dev.book.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController implements UserSwaggerController{

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

    @PutMapping("/categories")
    public ResponseEntity<?> updateUserCategories(@RequestBody UserCategoriesRequest userCategoriesRequest,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails){
        userService.updateUserCategories(userCategoriesRequest, userDetails);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(HttpServletRequest request, HttpServletResponse response,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails){
        userService.deleteUser(request, response, userDetails);
        return ResponseEntity.ok().build();
    }
}
