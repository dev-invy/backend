package com.invy.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * API 응답을 표준화하기 위한 DTO 클래스
 * - 응답 상태(성공/실패)
 * - 메시지
 * - 데이터
 * @param <T> 응답 데이터 타입
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    /**
     * 성공 응답 생성 (데이터 포함)
     * @param data 응답 데이터
     * @return ApiResponse 객체
     * @param <T> 데이터 타입
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "성공적으로 처리되었습니다.", data);
    }

    /**
     * 성공 응답 생성 (메시지와 데이터 포함)
     * @param message 성공 메시지
     * @param data 응답 데이터
     * @return ApiResponse 객체
     * @param <T> 데이터 타입
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * 성공 응답 생성 (메시지만 포함)
     * @param message 성공 메시지
     * @return ApiResponse 객체
     */
    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    /**
     * 오류 응답 생성
     * @param message 오류 메시지
     * @return ApiResponse 객체
     */
    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}