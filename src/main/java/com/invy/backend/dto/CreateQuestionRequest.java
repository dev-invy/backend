package com.invy.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 질문 생성 요청을 위한 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionRequest {

    @NotBlank(message = "질문 제목은 필수입니다.")
    private String title;

    @NotBlank(message = "질문 내용은 필수입니다.")
    private String content;

    @NotBlank(message = "기본 답변은 필수입니다.")
    private String defaultAnswer;

    @NotNull(message = "카테고리 ID는 필수입니다.")
    private Long categoryId;

    private List<String> keywords;
}