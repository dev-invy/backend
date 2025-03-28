package com.invy.backend.config.security;

import com.invy.backend.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 인증된 사용자의 정보를 담는 클래스
 * Spring Security의 UserDetails와 OAuth2User를 모두 구현하여
 * 일반 로그인과 OAuth2 로그인 모두 지원
 */
@Getter
public class UserPrincipal implements OAuth2User, UserDetails {
    private Long id;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(Long id, String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.authorities = authorities;
    }

    /**
     * User 엔티티로부터 UserPrincipal 객체 생성
     * @param user 사용자 엔티티
     * @return UserPrincipal 객체
     */
    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority(user.getRole().name()));

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                authorities
        );
    }

    /**
     * User 엔티티와 OAuth2 속성으로부터 UserPrincipal 객체 생성
     * @param user 사용자 엔티티
     * @param attributes OAuth2 인증 속성
     * @return UserPrincipal 객체
     */
    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    /**
     * OAuth2 인증 속성 설정
     * @param attributes OAuth2 인증 속성
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getPassword() {
        return null; // OAuth2 사용자는 비밀번호가 없음
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}