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
 * 질문 상세 정보를 전달하기 위한 DTO 클래스
 * 질문 상세 조회에 사용
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDetailDto {
    private Long id;
    private String title;
    private String content;
    private String defaultAnswer;
    private CategoryDto category;
    private List<KeywordDto> keywords;
    private List<AnswerDto> answers;
    private int lgtmCount;
    private boolean bookmarked;
    private boolean lgtmReacted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Question 엔티티를 QuestionDetailDto로 변환
     * @param question 질문 엔티티
     * @param bookmarked 북마크 여부
     * @param lgtmReacted LGTM 반응 여부
     * @param answers 답변 목록
     * @return QuestionDetailDto 객체
     */
    public static QuestionDetailDto fromEntity(Question question, boolean bookmarked, boolean lgtmReacted, List<AnswerDto> answers) {
        return QuestionDetailDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .defaultAnswer(question.getDefaultAnswer())
                .category(CategoryDto.fromEntity(question.getCategory()))
                .keywords(question.getKeywords().stream().map(KeywordDto::fromEntity).collect(Collectors.toList()))
                .answers(answers)
                .lgtmCount(question.getLgtmCount())
                .bookmarked(bookmarked)
                .lgtmReacted(lgtmReacted)
                .createdAt(question.getCreatedAt())
                .updatedAt(question.getUpdatedAt())
                .build();
    }
}