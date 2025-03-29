package com.invy.backend.service;

import com.invy.backend.dto.QuestionDto;
import com.invy.backend.entity.Category;
import com.invy.backend.entity.Keyword;
import com.invy.backend.entity.Question;
import com.invy.backend.repository.AnswerRepository;
import com.invy.backend.repository.CategoryRepository;
import com.invy.backend.repository.KeywordRepository;
import com.invy.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 관리자 기능을 제공하는 서비스
 * - 질문 등록/수정/삭제
 * - 답변 삭제
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final KeywordRepository keywordRepository;
    private final AnswerRepository answerRepository;

    /**
     * 질문 등록
     * @param title 질문 제목
     * @param content 질문 내용
     * @param defaultAnswer 기본 답변 내용
     * @param categoryId 카테고리 ID
     * @param keywordNames 키워드 이름 목록
     * @return 등록된 질문 정보
     */
    @Transactional
    public QuestionDto createQuestion(String title, String content, String defaultAnswer,
                                      Long categoryId, List<String> keywordNames) {
        // 카테고리 조회
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        // 키워드 처리 - 존재하면 조회, 없으면 생성
        Set<Keyword> keywords = new HashSet<>();
        for (String keywordName : keywordNames) {
            Keyword keyword = keywordRepository.findByName(keywordName)
                    .orElseGet(() -> {
                        Keyword newKeyword = Keyword.builder()
                                .name(keywordName)
                                .build();
                        return keywordRepository.save(newKeyword);
                    });
            keywords.add(keyword);
        }

        // 질문 생성 및 저장
        Question question = Question.builder()
                .title(title)
                .content(content)
                .defaultAnswer(defaultAnswer)
                .category(category)
                .keywords(keywords)
                .lgtmCount(0)
                .build();

        question = questionRepository.save(question);

        return QuestionDto.fromEntity(question, false);
    }

    /**
     * 질문 수정
     * @param questionId 질문 ID
     * @param title 질문 제목
     * @param content 질문 내용
     * @param defaultAnswer 기본 답변 내용
     * @param categoryId 카테고리 ID
     * @param keywordNames 키워드 이름 목록
     * @return 수정된 질문 정보
     */
    @Transactional
    public QuestionDto updateQuestion(Long questionId, String title, String content,
                                      String defaultAnswer, Long categoryId, List<String> keywordNames) {
        // 질문 조회
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));

        // 카테고리 조회 및 설정
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
            question.setCategory(category);
        }

        // 키워드 처리
        if (keywordNames != null && !keywordNames.isEmpty()) {
            Set<Keyword> keywords = new HashSet<>();
            for (String keywordName : keywordNames) {
                Keyword keyword = keywordRepository.findByName(keywordName)
                        .orElseGet(() -> {
                            Keyword newKeyword = Keyword.builder()
                                    .name(keywordName)
                                    .build();
                            return keywordRepository.save(newKeyword);
                        });
                keywords.add(keyword);
            }
            question.setKeywords(keywords);
        }

        // 필드 업데이트
        if (title != null) question.setTitle(title);
        if (content != null) question.setContent(content);
        if (defaultAnswer != null) question.setDefaultAnswer(defaultAnswer);

        question = questionRepository.save(question);

        return QuestionDto.fromEntity(question, false);
    }

    /**
     * 질문 삭제
     * @param questionId 질문 ID
     */
    @Transactional
    public void deleteQuestion(Long questionId) {
        // 질문 존재 여부 확인
        if (!questionRepository.existsById(questionId)) {
            throw new IllegalArgumentException("질문을 찾을 수 없습니다.");
        }

        // 질문 삭제 (연관된 북마크, 답변 등은 cascade 설정에 따라 자동 삭제)
        questionRepository.deleteById(questionId);
    }

    /**
     * 답변 삭제
     * @param answerId 답변 ID
     */
    @Transactional
    public void deleteAnswer(Long answerId) {
        // 답변 존재 여부 확인
        if (!answerRepository.existsById(answerId)) {
            throw new IllegalArgumentException("답변을 찾을 수 없습니다.");
        }

        // 답변 삭제
        answerRepository.deleteById(answerId);
    }
}