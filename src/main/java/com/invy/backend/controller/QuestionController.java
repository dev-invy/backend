package com.invy.backend.controller;

import com.invy.backend.dto.ApiResponse;
import com.invy.backend.dto.QuestionDetailDto;
import com.invy.backend.dto.QuestionDto;
import com.invy.backend.security.UserPrincipal;
import com.invy.backend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 질문 관련 API 엔드포인트를 제공하는 컨트롤러
 * - 질문 목록 조회
 * - 카테고리별 질문 조회
 * - 질문 상세 조회
 * - 북마크 토글
 * - LGTM 토글
 */
@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    /**
     * 모든 질문을 제목 기준 오름차순으로 조회 (페이징 처리)
     * @param userPrincipal 현재 인증된 사용자 (없을 수 있음)
     * @param pageable 페이징 정보
     * @return 질문 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<QuestionDto>>> getAllQuestions(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 20) Pageable pageable) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        Page<QuestionDto> questions = questionService.getAllQuestions(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(questions));
    }

    /**
     * 특정 카테고리의 질문을 제목 기준 오름차순으로 조회 (페이징 처리)
     * @param categoryId 카테고리 ID
     * @param userPrincipal 현재 인증된 사용자 (없을 수 있음)
     * @param pageable 페이징 정보
     * @return 질문 목록
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<QuestionDto>>> getQuestionsByCategory(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 20) Pageable pageable) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        Page<QuestionDto> questions = questionService.getQuestionsByCategory(categoryId, userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(questions));
    }

    /**
     * 질문 상세 정보 조회
     * @param questionId 질문 ID
     * @param userPrincipal 현재 인증된 사용자 (없을 수 있음)
     * @return 질문 상세 정보
     */
    @GetMapping("/{questionId}")
    public ResponseEntity<ApiResponse<QuestionDetailDto>> getQuestionDetail(
            @PathVariable Long questionId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        QuestionDetailDto questionDetail = questionService.getQuestionDetail(questionId, userId);
        return ResponseEntity.ok(ApiResponse.success(questionDetail));
    }

    /**
     * 질문 북마크 토글
     * @param questionId 질문 ID
     * @param userPrincipal 현재 인증된 사용자
     * @return 성공 메시지
     */
    @PostMapping("/{questionId}/bookmark")
    public ResponseEntity<ApiResponse<Void>> toggleBookmark(
            @PathVariable Long questionId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        questionService.toggleBookmark(questionId, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("북마크가 토글되었습니다."));
    }

    /**
     * 질문 LGTM 반응 토글
     * @param questionId 질문 ID
     * @param userPrincipal 현재 인증된 사용자
     * @return 성공 메시지
     */
    @PostMapping("/{questionId}/lgtm")
    public ResponseEntity<ApiResponse<Void>> toggleLgtm(
            @PathVariable Long questionId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        questionService.toggleLgtm(questionId, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("LGTM이 토글되었습니다."));
    }
}