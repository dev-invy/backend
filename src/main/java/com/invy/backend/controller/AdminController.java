package com.invy.backend.controller;

import com.invy.backend.dto.*;
import com.invy.backend.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자 기능을 위한 API 엔드포인트
 * - 면접 질문 등록/수정/삭제
 * - 답변 삭제
 */
@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 면접 질문 등록
     * @param request 질문 생성 요청 데이터
     * @return 생성된 질문 정보
     */
    @PostMapping("/questions")
    public ResponseEntity<ApiResponse<QuestionDto>> createQuestion(
            @Valid @RequestBody CreateQuestionRequest request) {
        QuestionDto questionDto = adminService.createQuestion(
                request.getTitle(),
                request.getContent(),
                request.getDefaultAnswer(),
                request.getCategoryId(),
                request.getKeywords()
        );
        return ResponseEntity.ok(ApiResponse.success("질문이 등록되었습니다.", questionDto));
    }

    /**
     * 면접 질문 수정
     * @param questionId 질문 ID
     * @param request 질문 수정 요청 데이터
     * @return 수정된 질문 정보
     */
    @PutMapping("/questions/{questionId}")
    public ResponseEntity<ApiResponse<QuestionDto>> updateQuestion(
            @PathVariable Long questionId,
            @RequestBody UpdateQuestionRequest request) {
        QuestionDto questionDto = adminService.updateQuestion(
                questionId,
                request.getTitle(),
                request.getContent(),
                request.getDefaultAnswer(),
                request.getCategoryId(),
                request.getKeywords()
        );
        return ResponseEntity.ok(ApiResponse.success("질문이 수정되었습니다.", questionDto));
    }

    /**
     * 면접 질문 삭제
     * @param questionId 질문 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable Long questionId) {
        adminService.deleteQuestion(questionId);
        return ResponseEntity.ok(ApiResponse.success("질문이 삭제되었습니다."));
    }

    /**
     * 답변 삭제
     * @param answerId 답변 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<ApiResponse<Void>> deleteAnswer(@PathVariable Long answerId) {
        adminService.deleteAnswer(answerId);
        return ResponseEntity.ok(ApiResponse.success("답변이 삭제되었습니다."));
    }
}