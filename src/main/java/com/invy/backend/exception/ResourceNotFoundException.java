package com.invy.backend.exception;

import org.springframework.http.HttpStatus;

/**
 * 요청한 리소스를 찾을 수 없을 때 사용하는 예외 클래스
 * - 404 Not Found 상태 코드 반환
 */
public class ResourceNotFoundException extends BusinessException {

    /**
     * 리소스를 찾을 수 없는 예외 생성
     * @param message 오류 메시지
     */
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    /**
     * 특정 리소스 유형과 ID에 대한 예외 생성
     * @param resourceName 리소스 유형 (예: "질문", "답변")
     * @param id 리소스 ID
     */
    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " ID: " + id + "를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}