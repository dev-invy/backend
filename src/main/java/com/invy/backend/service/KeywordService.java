package com.invy.backend.service;

import com.invy.backend.dto.KeywordDto;
import com.invy.backend.entity.Keyword;
import com.invy.backend.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 키워드 관련 비즈니스 로직을 처리하는 서비스
 * - 키워드 목록 조회
 * - 키워드 검색
 */
@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    /**
     * 모든 키워드를 이름 기준 오름차순으로 조회 (페이징 처리)
     * @param pageable 페이징 정보
     * @return 키워드 페이지 객체
     */
    @Transactional(readOnly = true)
    public Page<KeywordDto> getAllKeywords(Pageable pageable) {
        Page<Keyword> keywords = keywordRepository.findAllByOrderByNameAsc(pageable);
        return keywords.map(KeywordDto::fromEntity);
    }

    /**
     * 키워드 이름에 특정 문자열이 포함된 키워드를 이름 기준 오름차순 조회 (페이징 처리)
     * @param query 검색할 키워드 이름 (부분 일치)
     * @param pageable 페이징 정보
     * @return 키워드 페이지 객체
     */
    @Transactional(readOnly = true)
    public Page<KeywordDto> searchKeywords(String query, Pageable pageable) {
        Page<Keyword> keywords = keywordRepository.findByNameContainingOrderByNameAsc(query, pageable);
        return keywords.map(KeywordDto::fromEntity);
    }
}