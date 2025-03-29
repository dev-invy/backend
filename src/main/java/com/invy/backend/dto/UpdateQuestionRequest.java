package com.invy.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 질문 수정 요청을 위한 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuestionRequest {

    private String title;
    private String content;
    private String defaultAnswer;
    private Long categoryId;
    private List<String> keywords;
}