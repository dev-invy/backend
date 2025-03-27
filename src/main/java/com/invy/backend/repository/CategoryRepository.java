package com.invy.backend.repository;

import com.invy.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 카테고리(Category) 엔티티에 접근하기 위한 repository 인터페이스
 * - 카테고리 이름으로 조회
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 카테고리 이름으로 카테고리 조회
     * @param name 카테고리 이름
     * @return 카테고리 Optional 객체
     */
    Optional<Category> findByName(String name);
}