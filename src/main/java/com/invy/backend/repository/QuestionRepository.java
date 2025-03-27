package com.invy.backend.repository;

import com.invy.backend.entity.Category;
import com.invy.backend.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 면접 질문(Question) 엔티티에 접근하기 위한 repository 인터페이스
 * - 제목 기준 정렬 조회
 * - 카테고리별 조회
 * - 키워드 검색
 * - 제목/내용 검색
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    /**
     * 모든 질문을 제목 기준 오름차순으로 조회 (페이징 처리)
     * @param pageable 페이징 정보
     * @return 질문 페이지 객체
     */
    Page<Question> findAllByOrderByTitleAsc(Pageable pageable);

    /**
     * 특정 카테고리의 질문을 제목 기준 오름차순으로 조회 (페이징 처리)
     * @param category 카테고리 객체
     * @param pageable 페이징 정보
     * @return 질문 페이지 객체
     */
    Page<Question> findByCategoryOrderByTitleAsc(Category category, Pageable pageable);

    /**
     * 특정 키워드를 포함하는 질문 조회 (페이징 처리)
     * JPQL 쿼리를 직접 작성해 질문과 키워드 간의 다대다 관계를 처리
     * @param keyword 검색할 키워드
     * @param pageable 페이징 정보
     * @return 질문 페이지 객체
     */
    @Query("SELECT q FROM Question q JOIN q.keywords k WHERE k.name LIKE %:keyword%")
    Page<Question> findByKeywordContaining(String keyword, Pageable pageable);

    /**
     * 제목 또는 내용에 특정 검색어를 포함하는 질문 조회 (페이징 처리)
     * @param searchTerm 검색어
     * @param pageable 페이징 정보
     * @return 질문 페이지 객체
     */
    @Query("SELECT q FROM Question q WHERE q.title LIKE %:searchTerm% OR q.content LIKE %:searchTerm%")
    Page<Question> searchByTitleOrContent(String searchTerm, Pageable pageable);
}