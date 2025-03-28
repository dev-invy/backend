package com.invy.backend.security;

import java.util.Map;

/**
 * Apple OAuth2 응답에서 사용자 정보를 추출하는 구현 클래스
 * Apple은 첫 로그인 시에만 이름 정보를 제공하며,
 * 프로필 이미지를 제공하지 않는다는 특징이 있음
 */
public class AppleOAuth2UserInfo extends OAuth2UserInfo {

    public AppleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        // Apple은 첫 로그인 시에만 이름 정보를 제공
        return (String) attributes.getOrDefault("name", "");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        // Apple은 프로필 이미지를 제공하지 않음
        return null;
    }
}