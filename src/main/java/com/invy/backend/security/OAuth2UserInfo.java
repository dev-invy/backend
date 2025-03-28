package com.invy.backend.security;

import java.util.Map;

/**
 * 다양한 OAuth2 제공자(Google, Kakao, Apple)로부터 받은
 * 사용자 정보를 표준화하기 위한 추상 클래스
 */
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * OAuth2 제공자의 사용자 ID 반환
     */
    public abstract String getId();

    /**
     * 사용자 이름 반환
     */
    public abstract String getName();

    /**
     * 사용자 이메일 반환
     */
    public abstract String getEmail();

    /**
     * 사용자 프로필 이미지 URL 반환
     */
    public abstract String getImageUrl();
}