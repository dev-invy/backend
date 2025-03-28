package com.invy.backend.dto;

import com.invy.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보를 전달하기 위한 DTO 클래스
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String profileImage;
    private User.AuthProvider provider;
    private User.Role role;

    /**
     * User 엔티티를 UserDto로 변환
     * @param user 사용자 엔티티
     * @return UserDto 객체
     */
    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .provider(user.getProvider())
                .role(user.getRole())
                .build();
    }
}