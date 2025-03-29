package com.invy.backend.service;

import com.invy.backend.dto.AuthResponse;
import com.invy.backend.entity.User;
import com.invy.backend.exception.BusinessException;
import com.invy.backend.exception.ResourceNotFoundException;
import com.invy.backend.repository.UserRepository;
import com.invy.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 관련 비즈니스 로직을 처리하는 서비스
 * - 로그아웃
 * - 회원 탈퇴
 * - 토큰 갱신
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 로그아웃 처리
     * JWT 는 상태가 없으므로 서버에서 특별한 처리가 필요 없음
     * @param email 사용자 이메일
     */
    @Transactional
    public void logout(String email) {
        // JWT 는 상태가 없기 때문에 서버 측에서 특별히 처리할 것이 없음
        // 클라이언트에서 토큰을 삭제하도록 안내
    }

    /**
     * 회원 탈퇴 처리
     * @param userId 사용자 ID
     */
    @Transactional
    public void deleteAccount(Long userId) {
        // 사용자 존재 여부 확인
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("사용자", userId);
        }

        userRepository.deleteById(userId);
    }

    /**
     * 리프레시 토큰을 사용하여 새 액세스 토큰 발급
     * @param refreshToken 리프레시 토큰
     * @return AuthResponse 새로운 토큰 정보
     */
    @Transactional(readOnly = true)
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException("리프레시 토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        String email = jwtTokenProvider.getUserEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("이메일이 " + email + "인 사용자를 찾을 수 없습니다."));

        String newAccessToken = jwtTokenProvider.createAccessToken(email);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email);

        return new AuthResponse(newAccessToken, newRefreshToken, "Bearer");
    }
}