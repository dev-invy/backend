package com.invy.backend.service;

import com.invy.backend.dto.AnswerDto;
import com.invy.backend.entity.Answer;
import com.invy.backend.entity.Question;
import com.invy.backend.entity.Reaction;
import com.invy.backend.entity.User;
import com.invy.backend.exception.ResourceNotFoundException;
import com.invy.backend.repository.AnswerRepository;
import com.invy.backend.repository.QuestionRepository;
import com.invy.backend.repository.ReactionRepository;
import com.invy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 답변 관련 비즈니스 로직을 처리하는 서비스
 * - 답변 생성
 * - 답변 채택
 * - LGTM 토글
 */
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;

    /**
     * 답변 생성
     * @param questionId 질문 ID
     * @param userId 사용자 ID
     * @param content 답변 내용
     * @param isAnonymous 익명 여부
     * @return 생성된 답변 DTO
     */
    @Transactional
    public AnswerDto createAnswer(Long questionId, Long userId, String content, boolean isAnonymous) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("질문", questionId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자", userId));

        Answer answer = Answer.builder()
                .content(content)
                .question(question)
                .user(user)
                .isAnonymous(isAnonymous)
                .isSelected(false)
                .lgtmCount(0)
                .build();

        answer = answerRepository.save(answer);

        return AnswerDto.fromEntity(answer, false);
    }

    /**
     * 답변 채택 - 기존에 채택된 답변이 있으면 채택 해제 후 새로운 답변 채택
     * @param answerId 답변 ID
     * @param userId 사용자 ID (권한 확인용, 현재는 사용 X)
     */
    @Transactional
    public void selectAnswer(Long answerId, Long userId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("답변", answerId));

        Question question = answer.getQuestion();

        // 이미 선택된 답변이 있으면 선택 해제
        Answer previousSelected = answerRepository.findByQuestionAndIsSelectedTrue(question).orElse(null);
        if (previousSelected != null) {
            previousSelected.setSelected(false);
            answerRepository.save(previousSelected);
        }

        // 현재 답변을 선택 상태로 변경
        answer.setSelected(true);
        answerRepository.save(answer);

        // 기본 답변을 업데이트
        question.setDefaultAnswer(answer.getContent());
        questionRepository.save(question);
    }

    /**
     * 답변 LGTM 반응 토글
     * @param answerId 답변 ID
     * @param userId 사용자 ID
     */
    @Transactional
    public void toggleLgtm(Long answerId, Long userId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("답변", answerId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자", userId));

        Reaction reaction = reactionRepository.findByUserAndAnswer(user, answer).orElse(null);

        if (reaction != null) {
            // 이미 LGTM 을 눌렀으면 삭제하고 카운트 감소
            reactionRepository.delete(reaction);
            answer.setLgtmCount(answer.getLgtmCount() - 1);
        } else {
            // LGTM 이 없으면 생성하고 카운트 증가
            reaction = Reaction.builder()
                    .user(user)
                    .answer(answer)
                    .build();
            reactionRepository.save(reaction);
            answer.setLgtmCount(answer.getLgtmCount() + 1);
        }

        answerRepository.save(answer);
    }
}