package com.invy.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 답변 생성 요청을 위한 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnswerRequest {

    @NotNull(message = "질문 ID는 필수입니다.")
    private Long questionId;

    @NotBlank(message = "답변 내용은 필수입니다.")
    private String content;

    private boolean isAnonymous;
}