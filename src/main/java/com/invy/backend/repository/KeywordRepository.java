package com.invy.backend.repository;

import com.invy.backend.entity.Keyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 키워드(Keyword) 엔티티에 접근하기 위한 repository 인터페이스
 * - 키워드 이름으로 조회
 * - 모든 키워드 이름순 정렬 조회
 * - 키워드 이름 검색 (부분 일치)
 */
@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    /**
     * 키워드 이름으로 키워드 조회
     * @param name 키워드 이름
     * @return 키워드 Optional 객체
     */
    Optional<Keyword> findByName(String name);

    /**
     * 모든 키워드를 이름 기준 오름차순으로 조회 (페이징 처리)
     * @param pageable 페이징 정보
     * @return 키워드 페이지 객체
     */
    Page<Keyword> findAllByOrderByNameAsc(Pageable pageable);

    /**
     * 키워드 이름에 특정 문자열이 포함된 키워드를 이름 기준 오름차순 조회 (페이징 처리)
     * 유사도 기반 검색을 위해 사용
     * @param name 검색할 키워드 이름 (부분 일치)
     * @param pageable 페이징 정보
     * @return 키워드 페이지 객체
     */
    Page<Keyword> findByNameContainingOrderByNameAsc(String name, Pageable pageable);
}