package com.invy.backend.dto;

import com.invy.backend.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 답변 정보를 전달하기 위한 DTO 클래스
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDto {
    private Long id;
    private String content;
    private UserDto user;
    private boolean isAnonymous;
    private boolean isSelected;
    private int lgtmCount;
    private boolean lgtmReacted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Answer 엔티티를 AnswerDto로 변환
     * @param answer 답변 엔티티
     * @param lgtmReacted 현재 사용자의 LGTM 반응 여부
     * @return AnswerDto 객체
     */
    public static AnswerDto fromEntity(Answer answer, boolean lgtmReacted) {
        UserDto userDto = null;

        // 익명이 아닌 경우에만 사용자 정보를 포함
        if (!answer.isAnonymous()) {
            userDto = UserDto.fromEntity(answer.getUser());
        }

        return AnswerDto.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .user(userDto)
                .isAnonymous(answer.isAnonymous())
                .isSelected(answer.isSelected())
                .lgtmCount(answer.getLgtmCount())
                .lgtmReacted(lgtmReacted)
                .createdAt(answer.getCreatedAt())
                .updatedAt(answer.getUpdatedAt())
                .build();
    }
}