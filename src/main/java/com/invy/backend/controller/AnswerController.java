package com.invy.backend.controller;

import com.invy.backend.dto.AnswerDto;
import com.invy.backend.dto.ApiResponse;
import com.invy.backend.dto.CreateAnswerRequest;
import com.invy.backend.security.UserPrincipal;
import com.invy.backend.service.AnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 답변 관련 API 엔드포인트를 제공하는 컨트롤러
 * - 답변 생성
 * - 답변 채택
 * - LGTM 토글
 */
@RestController
@RequestMapping("/api/v1/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    /**
     * 답변 생성
     * @param request 답변 생성 요청 데이터
     * @param userPrincipal 현재 인증된 사용자
     * @return 생성된 답변 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AnswerDto>> createAnswer(
            @Valid @RequestBody CreateAnswerRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        AnswerDto answerDto = answerService.createAnswer(
                request.getQuestionId(),
                userPrincipal.getId(),
                request.getContent(),
                request.isAnonymous()
        );
        return ResponseEntity.ok(ApiResponse.success("답변이 등록되었습니다.", answerDto));
    }

    /**
     * 답변 채택
     * @param answerId 답변 ID
     * @param userPrincipal 현재 인증된 사용자
     * @return 성공 메시지
     */
    @PostMapping("/{answerId}/select")
    public ResponseEntity<ApiResponse<Void>> selectAnswer(
            @PathVariable Long answerId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        answerService.selectAnswer(answerId, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("답변이 채택되었습니다."));
    }

    /**
     * 답변 LGTM 반응 토글
     * @param answerId 답변 ID
     * @param userPrincipal 현재 인증된 사용자
     * @return 성공 메시지
     */
    @PostMapping("/{answerId}/lgtm")
    public ResponseEntity<ApiResponse<Void>> toggleLgtm(
            @PathVariable Long answerId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        answerService.toggleLgtm(answerId, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("LGTM이 토글되었습니다."));
    }
}