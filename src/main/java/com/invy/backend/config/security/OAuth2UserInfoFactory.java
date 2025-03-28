package com.invy.backend.config.security;

import com.invy.backend.entity.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

/**
 * 소셜 로그인 제공자에 따라 적절한 OAuth2UserInfo 구현체를 생성하는 팩토리 클래스
 */
public class OAuth2UserInfoFactory {

    /**
     * 소셜 로그인 제공자와 속성 정보를 받아 해당하는 OAuth2UserInfo 인스턴스 생성
     *
     * @param registrationId 소셜 로그인 제공자 ID (google, kakao, apple)
     * @param attributes 소셜 로그인 제공자로부터 받은 사용자 속성 정보
     * @return 제공자에 맞는 OAuth2UserInfo 구현체
     * @throws OAuth2AuthenticationException 지원하지 않는 소셜 로그인 제공자일 경우 발생
     */
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(User.AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(User.AuthProvider.KAKAO.toString())) {
            return new KakaoOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(User.AuthProvider.APPLE.toString())) {
            return new AppleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}