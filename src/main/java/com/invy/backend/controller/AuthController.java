package com.invy.backend.controller;

import com.invy.backend.dto.ApiResponse;
import com.invy.backend.dto.AuthResponse;
import com.invy.backend.security.UserPrincipal;
import com.invy.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 관련 요청을 처리하는 컨트롤러
 * - 로그아웃
 * - 회원 탈퇴
 * - 토큰 갱신
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 로그아웃 처리
     * @param userPrincipal 현재 인증된 사용자
     * @return ResponseEntity 응답 엔티티
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        authService.logout(userPrincipal.getEmail());
        return ResponseEntity.ok(ApiResponse.success("로그아웃되었습니다."));
    }

    /**
     * 회원 탈퇴 처리
     * @param userPrincipal 현재 인증된 사용자
     * @return ResponseEntity 응답 엔티티
     */
    @DeleteMapping("/user")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        authService.deleteAccount(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("계정이 삭제되었습니다."));
    }

    /**
     * 리프레시 토큰을 사용하여 새 액세스 토큰 발급
     * @param refreshToken 리프레시 토큰
     * @return ResponseEntity 응답 엔티티
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestParam("refresh_token") String refreshToken) {
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }
}