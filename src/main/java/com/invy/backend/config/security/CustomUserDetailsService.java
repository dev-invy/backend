package com.invy.backend.config.security;

import com.invy.backend.entity.User;
import com.invy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Spring Security의 UserDetailsService 구현체
 * 사용자 이메일로 사용자 정보를 로드하는 기능 제공
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 사용자 이메일로 UserDetails 객체 로드
     * @param email 사용자 이메일
     * @return Spring Security UserDetails 객체
     * @throws UsernameNotFoundException 이메일에 해당하는 사용자가 없을 경우 발생
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                "", // OAuth2 사용자는 비밀번호가 없으므로 빈 문자열
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}