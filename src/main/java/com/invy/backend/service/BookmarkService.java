package com.invy.backend.service;

import com.invy.backend.dto.BookmarkDto;
import com.invy.backend.entity.Bookmark;
import com.invy.backend.entity.Category;
import com.invy.backend.entity.User;
import com.invy.backend.repository.BookmarkRepository;
import com.invy.backend.repository.CategoryRepository;
import com.invy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 북마크 관련 비즈니스 로직을 처리하는 서비스
 * - 사용자별 북마크 조회
 * - 카테고리별 북마크 조회
 */
@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 사용자별 북마크 조회
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 북마크 페이지 객체
     */
    @Transactional(readOnly = true)
    public Page<BookmarkDto> getUserBookmarks(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Page<Bookmark> bookmarks = bookmarkRepository.findByUser(user, pageable);
        return bookmarks.map(BookmarkDto::fromEntity);
    }

    /**
     * 사용자별, 카테고리별 북마크 조회
     * @param userId 사용자 ID
     * @param categoryId 카테고리 ID
     * @param pageable 페이징 정보
     * @return 북마크 페이지 객체
     */
    @Transactional(readOnly = true)
    public Page<BookmarkDto> getUserBookmarksByCategory(Long userId, Long categoryId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        Page<Bookmark> bookmarks = bookmarkRepository.findByUserAndCategory(user, category, pageable);
        return bookmarks.map(BookmarkDto::fromEntity);
    }
}