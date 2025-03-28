package com.invy.backend.dto;

import com.invy.backend.entity.Keyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 키워드 정보를 전달하기 위한 DTO 클래스
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordDto {
    private Long id;
    private String name;

    /**
     * Keyword 엔티티를 KeywordDto로 변환
     * @param keyword 키워드 엔티티
     * @return KeywordDto 객체
     */
    public static KeywordDto fromEntity(Keyword keyword) {
        return KeywordDto.builder()
                .id(keyword.getId())
                .name(keyword.getName())
                .build();
    }
}