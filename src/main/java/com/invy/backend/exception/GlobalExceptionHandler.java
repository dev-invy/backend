package com.invy.backend.exception;

import com.invy.backend.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 애플리케이션 전체에서 발생하는 예외를 처리하는 전역 예외 핸들러
 * - 다양한 종류의 예외에 대해 적절한 HTTP 상태 코드와 메시지 반환
 * - 예외 발생 시 로깅
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * IllegalArgumentException 처리
     * 유효하지 않은 인자 값 전달 시 발생
     * @param e 발생한 예외 객체
     * @return 400 Bad Request 응답
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException: {}", e.getMessage(), e);
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }

    /**
     * MethodArgumentNotValidException 처리
     * @Valid 어노테이션으로 검증 실패 시 발생
     * @param e 발생한 예외 객체
     * @return 400 Bad Request 응답 (필드별 오류 메시지 포함)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(this::buildFieldErrorMessage)
                .collect(Collectors.joining(", "));

        log.error("Validation error: {}", errorMessage, e);
        return ResponseEntity.badRequest().body(ApiResponse.error(errorMessage));
    }

    /**
     * 필드 오류 메시지 생성 헬퍼 메서드
     * @param error 필드 오류 객체
     * @return 포맷된 오류 메시지
     */
    private String buildFieldErrorMessage(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }

    /**
     * AuthenticationException 처리
     * 인증 실패 시 발생
     * @param e 발생한 예외 객체
     * @return 401 Unauthorized 응답
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException e) {
        log.error("Authentication error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("인증에 실패했습니다: " + e.getMessage()));
    }

    /**
     * AccessDeniedException 처리
     * 권한 부족으로 접근 거부 시 발생
     * @param e 발생한 예외 객체
     * @return 403 Forbidden 응답
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access denied: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("접근 권한이 없습니다."));
    }

    /**
     * BadCredentialsException 처리
     * 잘못된 인증 정보 제공 시 발생
     * @param e 발생한 예외 객체
     * @return 401 Unauthorized 응답
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException e) {
        log.error("Bad credentials: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("인증 정보가 올바르지 않습니다."));
    }

    /**
     * 비즈니스 로직 예외 처리용 커스텀 예외 처리
     * @param e 발생한 예외 객체
     * @return 400 Bad Request 응답
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        log.error("Business error: {}", e.getMessage(), e);
        return ResponseEntity.status(e.getStatus()).body(ApiResponse.error(e.getMessage()));
    }

    /**
     * 그 외 모든 예외 처리
     * @param e 발생한 예외 객체
     * @return 500 Internal Server Error 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unexpected error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("서버에서 오류가 발생했습니다. 관리자에게 문의해 주세요."));
    }
}