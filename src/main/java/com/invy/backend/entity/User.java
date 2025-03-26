package com.invy.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 사용자 정보를 저장하는 엔티티 클래스
 * - 소셜 로그인 정보 (Google, Kakao, Apple)
 * - 사용자 기본 정보 (이메일, 이름, 프로필 이미지)
 * - 권한 정보 (일반 사용자, 관리자)
 */
@Entity
@Table(name = "users") // 'user'는 SQL 예약어이므로 'users'로 테이블명 지정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private String profileImage;

    @Enumerated(EnumType.STRING) // enum 값을 문자열로 저장
    private AuthProvider provider;

    private String providerId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Bookmark> bookmarks = new HashSet<>();

    @CreationTimestamp // 엔티티 생성 시간 자동 기록
    private LocalDateTime createdAt;

    @UpdateTimestamp // 엔티티 수정 시간 자동 기록
    private LocalDateTime updatedAt;

    /**
     * 인증 제공자 열거형
     * - LOCAL: 자체 로그인 (향후 구현 예정)
     * - GOOGLE, KAKAO, APPLE: 소셜 로그인
     */
    public enum AuthProvider {
        LOCAL, GOOGLE, KAKAO, APPLE
    }

    /**
     * 사용자 역할 열거형
     * - ROLE_USER: 일반 사용자
     * - ROLE_ADMIN: 관리자
     */
    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }
}