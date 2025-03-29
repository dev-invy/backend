package com.invy.backend.controller;

import com.invy.backend.dto.ApiResponse;
import com.invy.backend.dto.KeywordDto;
import com.invy.backend.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 키워드 관련 API 엔드포인트를 제공하는 컨트롤러
 * - 키워드 목록 조회
 * - 키워드 검색
 */
@RestController
@RequestMapping("/api/v1/keywords")
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    /**
     * 모든 키워드를 이름 기준 오름차순으로 조회 (페이징 처리)
     * @param pageable 페이징 정보
     * @return 키워드 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<KeywordDto>>> getAllKeywords(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<KeywordDto> keywords = keywordService.getAllKeywords(pageable);
        return ResponseEntity.ok(ApiResponse.success(keywords));
    }

    /**
     * 키워드 이름에 특정 문자열이 포함된 키워드 검색 (페이징 처리)
     * @param query 검색할 키워드 이름 (부분 일치)
     * @param pageable 페이징 정보
     * @return 검색된 키워드 목록
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<KeywordDto>>> searchKeywords(
            @RequestParam String query,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<KeywordDto> keywords = keywordService.searchKeywords(query, pageable);
        return ResponseEntity.ok(ApiResponse.success(keywords));
    }
}