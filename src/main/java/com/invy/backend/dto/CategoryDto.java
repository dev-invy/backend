package com.invy.backend.dto;

import com.invy.backend.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카테고리 정보를 전달하기 위한 DTO 클래스
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String name;
    private String description;

    /**
     * Category 엔티티를 CategoryDto로 변환
     * @param category 카테고리 엔티티
     * @return CategoryDto 객체
     */
    public static CategoryDto fromEntity(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}