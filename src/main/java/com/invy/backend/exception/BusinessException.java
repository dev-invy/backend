package com.invy.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 비즈니스 로직 관련 예외를 처리하기 위한 커스텀 예외 클래스
 * - HTTP 상태 코드 지정 가능
 * - 구체적인 오류 메시지 제공
 */
@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    /**
     * 기본 상태 코드(400 Bad Request)로 예외 생성
     * @param message 오류 메시지
     */
    public BusinessException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * 지정된 상태 코드로 예외 생성
     * @param message 오류 메시지
     * @param status HTTP 상태 코드
     */
    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}