package com.invy.backend.controller;

import com.invy.backend.dto.ApiResponse;
import com.invy.backend.dto.BookmarkDto;
import com.invy.backend.security.UserPrincipal;
import com.invy.backend.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 북마크 관련 API 엔드포인트를 제공하는 컨트롤러
 * - 북마크 목록 조회
 * - 카테고리별 북마크 조회
 */
@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    /**
     * 현재 사용자의 북마크 목록 조회 (페이징 처리)
     * @param userPrincipal 현재 인증된 사용자
     * @param pageable 페이징 정보
     * @return 북마크 목록
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookmarkDto>>> getUserBookmarks(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<BookmarkDto> bookmarks = bookmarkService.getUserBookmarks(userPrincipal.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(bookmarks));
    }

    /**
     * 현재 사용자의 특정 카테고리 북마크 목록 조회 (페이징 처리)
     * @param categoryId 카테고리 ID
     * @param userPrincipal 현재 인증된 사용자
     * @param pageable 페이징 정보
     * @return 북마크 목록
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<BookmarkDto>>> getUserBookmarksByCategory(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<BookmarkDto> bookmarks = bookmarkService.getUserBookmarksByCategory(userPrincipal.getId(), categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.success(bookmarks));
    }
}