package com.invy.backend.service;

import com.invy.backend.dto.AnswerDto;
import com.invy.backend.dto.QuestionDetailDto;
import com.invy.backend.dto.QuestionDto;
import com.invy.backend.entity.*;
import com.invy.backend.exception.ResourceNotFoundException;
import com.invy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 질문 관련 비즈니스 로직을 처리하는 서비스
 * - 질문 목록 조회
 * - 질문 상세 조회
 * - 북마크 토글
 * - LGTM 토글
 */
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final BookmarkRepository bookmarkRepository;
    private final AnswerRepository answerRepository;
    private final ReactionRepository reactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 모든 질문을 제목 기준 오름차순으로 조회 (페이징 처리)
     * @param userId 사용자 ID (null 가능)
     * @param pageable 페이징 정보
     * @return 질문 페이지 객체
     */
    @Transactional(readOnly = true)
    public Page<QuestionDto> getAllQuestions(Long userId, Pageable pageable) {
        Page<Question> questions = questionRepository.findAllByOrderByTitleAsc(pageable);

        return questions.map(question -> {
            boolean bookmarked = false;
            if (userId != null) {
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    bookmarked = bookmarkRepository.existsByUserAndQuestion(user, question);
                }
            }
            return QuestionDto.fromEntity(question, bookmarked);
        });
    }

    /**
     * 특정 카테고리의 질문을 제목 기준 오름차순으로 조회 (페이징 처리)
     * @param categoryId 카테고리 ID
     * @param userId 사용자 ID (null 가능)
     * @param pageable 페이징 정보
     * @return 질문 페이지 객체
     */
    @Transactional(readOnly = true)
    public Page<QuestionDto> getQuestionsByCategory(Long categoryId, Long userId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        Page<Question> questions = questionRepository.findByCategoryOrderByTitleAsc(category, pageable);

        return questions.map(question -> {
            boolean bookmarked = false;
            if (userId != null) {
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    bookmarked = bookmarkRepository.existsByUserAndQuestion(user, question);
                }
            }
            return QuestionDto.fromEntity(question, bookmarked);
        });
    }

    /**
     * 질문 상세 정보 조회
     * @param questionId 질문 ID
     * @param userId 사용자 ID (null 가능)
     * @return 질문 상세 정보
     */
    @Transactional(readOnly = true)
    public QuestionDetailDto getQuestionDetail(Long questionId, Long userId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("질문", questionId));

        boolean bookmarked = false;
        boolean lgtmReacted = false;

        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("사용자", userId));

            bookmarked = bookmarkRepository.existsByUserAndQuestion(user, question);
            lgtmReacted = reactionRepository.existsByUserAndQuestion(user, question);
        }

        List<Answer> answers = answerRepository.findByQuestionOrderByIsSelectedDescLgtmCountDescCreatedAtDesc(question);

        List<AnswerDto> answerDtos = answers.stream()
                .map(answer -> {
                    boolean answerLgtmReacted = false;
                    if (userId != null) {
                        User user = userRepository.findById(userId).orElse(null);
                        if (user != null) {
                            answerLgtmReacted = reactionRepository.existsByUserAndAnswer(user, answer);
                        }
                    }
                    return AnswerDto.fromEntity(answer, answerLgtmReacted);
                })
                .collect(Collectors.toList());

        return QuestionDetailDto.fromEntity(question, bookmarked, lgtmReacted, answerDtos);
    }

    /**
     * 질문 북마크 토글
     * @param questionId 질문 ID
     * @param userId 사용자 ID
     */
    @Transactional
    public void toggleBookmark(Long questionId, Long userId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("질문", questionId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자", userId));

        Bookmark bookmark = bookmarkRepository.findByUserAndQuestion(user, question).orElse(null);

        if (bookmark != null) {
            // 북마크가 이미 존재하면 삭제
            bookmarkRepository.delete(bookmark);
        } else {
            // 북마크가 없으면 생성
            bookmark = Bookmark.builder()
                    .user(user)
                    .question(question)
                    .build();
            bookmarkRepository.save(bookmark);
        }
    }

    /**
     * 질문 LGTM 반응 토글
     * @param questionId 질문 ID
     * @param userId 사용자 ID
     */
    @Transactional
    public void toggleLgtm(Long questionId, Long userId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("질문", questionId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자", userId));


        Reaction reaction = reactionRepository.findByUserAndQuestion(user, question).orElse(null);

        if (reaction != null) {
            // 이미 LGTM 을 눌렀으면 삭제하고 카운트 감소
            reactionRepository.delete(reaction);
            question.setLgtmCount(question.getLgtmCount() - 1);
        } else {
            // LGTM 이 없으면 생성하고 카운트 증가
            reaction = Reaction.builder()
                    .user(user)
                    .question(question)
                    .build();
            reactionRepository.save(reaction);
            question.setLgtmCount(question.getLgtmCount() + 1);
        }

        questionRepository.save(question);
    }
}