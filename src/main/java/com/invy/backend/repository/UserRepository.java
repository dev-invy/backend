package com.invy.backend.repository;

import com.invy.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자(User) 엔티티에 접근하기 위한 repository 인터페이스
 * - 이메일 기반 사용자 조회
 * - 소셜 로그인 제공자 및 ID 기반 사용자 조회
 * - 이메일 존재 여부 확인
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일로 사용자 조회
     * @param email 사용자 이메일
     * @return 사용자 Optional 객체
     */
    Optional<User> findByEmail(String email);

    /**
     * 소셜 로그인 정보로 사용자 조회
     * @param provider 소셜 로그인 제공자 (Google, Kakao, Apple)
     * @param providerId 소셜 로그인 제공자의 사용자 ID
     * @return 사용자 Optional 객체
     */
    Optional<User> findByProviderAndProviderId(User.AuthProvider provider, String providerId);

    /**
     * 이메일 존재 여부 확인
     * @param email 사용자 이메일
     * @return 존재 여부 (true/false)
     */
    boolean existsByEmail(String email);
}