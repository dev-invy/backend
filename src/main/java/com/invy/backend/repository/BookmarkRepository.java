package com.invy.backend.repository;

import com.invy.backend.entity.Bookmark;
import com.invy.backend.entity.Category;
import com.invy.backend.entity.Question;
import com.invy.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 북마크(Bookmark) 엔티티에 접근하기 위한 repository 인터페이스
 * - 사용자와 질문으로 북마크 조회
 * - 사용자별 북마크 조회
 * - 사용자별, 카테고리별 북마크 조회
 * - 북마크 존재 여부 확인
 */
@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    /**
     * 특정 사용자가 특정 질문에 대해 생성한 북마크 조회
     * @param user 사용자 객체
     * @param question 질문 객체
     * @return 북마크 Optional 객체
     */
    Optional<Bookmark> findByUserAndQuestion(User user, Question question);

    /**
     * 특정 사용자의 모든 북마크 조회 (페이징 처리)
     * @param user 사용자 객체
     * @param pageable 페이징 정보
     * @return 북마크 페이지 객체
     */
    Page<Bookmark> findByUser(User user, Pageable pageable);

    /**
     * 특정 사용자의 특정 카테고리에 속하는 북마크 조회 (페이징 처리)
     * JPQL 쿼리를 사용해 질문의 카테고리까지 조건에 포함
     * @param user 사용자 객체
     * @param category 카테고리 객체
     * @param pageable 페이징 정보
     * @return 북마크 페이지 객체
     */
    @Query("SELECT b FROM Bookmark b WHERE b.user = :user AND b.question.category = :category")
    Page<Bookmark> findByUserAndCategory(User user, Category category, Pageable pageable);

    /**
     * 특정 사용자가 특정 질문을 북마크했는지 확인
     * @param user 사용자 객체
     * @param question 질문 객체
     * @return 북마크 존재 여부 (true/false)
     */
    boolean existsByUserAndQuestion(User user, Question question);
}