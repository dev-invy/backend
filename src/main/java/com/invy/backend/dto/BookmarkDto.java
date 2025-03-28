package com.invy.backend.dto;

import com.invy.backend.entity.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 북마크 정보를 전달하기 위한 DTO 클래스
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkDto {
    private Long id;
    private QuestionDto question;
    private LocalDateTime createdAt;

    /**
     * Bookmark 엔티티를 BookmarkDto로 변환
     * @param bookmark 북마크 엔티티
     * @return BookmarkDto 객체
     */
    public static BookmarkDto fromEntity(Bookmark bookmark) {
        return BookmarkDto.builder()
                .id(bookmark.getId())
                .question(QuestionDto.fromEntity(bookmark.getQuestion(), true))
                .createdAt(bookmark.getCreatedAt())
                .build();
    }
}