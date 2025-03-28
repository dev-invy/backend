package com.invy.backend.security;

import com.invy.backend.entity.User;
import com.invy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * OAuth2 인증 후 사용자 정보를 처리하는 서비스
 * - 소셜 로그인 제공자로부터 받은 사용자 정보를 처리
 * - 신규 사용자는 등록, 기존 사용자는 정보 업데이트
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    /**
     * OAuth2 인증 후 사용자 정보 로드
     * @param oAuth2UserRequest OAuth2 사용자 요청 정보
     * @return OAuth2User 인증된 사용자 정보
     * @throws OAuth2AuthenticationException OAuth2 인증 예외
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // 인증 프로세스 중 예외 발생
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * OAuth2 사용자 정보 처리
     * - 이메일이 없는 경우 예외 발생
     * - 기존 사용자는 정보 업데이트
     * - 신규 사용자는 등록
     *
     * @param oAuth2UserRequest OAuth2 사용자 요청 정보
     * @param oAuth2User OAuth2 사용자 정보
     * @return UserPrincipal 처리된 사용자 정보
     */
    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();

            // 사용자가 다른 소셜 계정으로 가입한 경우 예외 발생
            if (!user.getProvider().equals(
                    User.AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()))) {
                throw new OAuth2AuthenticationException(
                        "You've signed up with " + user.getProvider() + ". Please use that to login."
                );
            }

            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    /**
     * 신규 사용자 등록
     * @param oAuth2UserRequest OAuth2 사용자 요청 정보
     * @param oAuth2UserInfo OAuth2 사용자 정보
     * @return User 등록된 사용자 엔티티
     */
    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = User.builder()
                .provider(User.AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()))
                .providerId(oAuth2UserInfo.getId())
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .profileImage(oAuth2UserInfo.getImageUrl())
                .role(User.Role.ROLE_USER)
                .build();

        return userRepository.save(user);
    }

    /**
     * 기존 사용자 정보 업데이트
     * @param existingUser 기존 사용자 엔티티
     * @param oAuth2UserInfo OAuth2 사용자 정보
     * @return User 업데이트된 사용자 엔티티
     */
    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setProfileImage(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}