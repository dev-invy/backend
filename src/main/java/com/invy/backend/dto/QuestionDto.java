package com.invy.backend.dto;

import com.invy.backend.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 질문 정보를 전달하기 위한 DTO 클래스
 * 질문 목록 조회 등에 사용
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDto {
    private Long id;
    private String title;
    private String content;
    private String defaultAnswer;
    private CategoryDto category;
    private List<KeywordDto> keywords;
    private int lgtmCount;
    private boolean bookmarked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Question 엔티티를 QuestionDto로 변환
     * @param question 질문 엔티티
     * @param bookmarked 북마크 여부
     * @return QuestionDto 객체
     */
    public static QuestionDto fromEntity(Question question, boolean bookmarked) {
        return QuestionDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .defaultAnswer(question.getDefaultAnswer())
                .category(CategoryDto.fromEntity(question.getCategory()))
                .keywords(question.getKeywords().stream().map(KeywordDto::fromEntity).collect(Collectors.toList()))
                .lgtmCount(question.getLgtmCount())
                .bookmarked(bookmarked)
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }
}