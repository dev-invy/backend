package com.invy.backend.repository;

import com.invy.backend.entity.Answer;
import com.invy.backend.entity.Question;
import com.invy.backend.entity.Reaction;
import com.invy.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 반응(Reaction) 엔티티에 접근하기 위한 repository 인터페이스
 * - 사용자의 질문/답변에 대한 반응 조회
 * - 질문/답변에 대한 반응 수 계산
 * - 사용자의 특정 질문/답변에 대한 반응 존재 여부 확인
 */
@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    /**
     * 사용자가 특정 질문에 남긴 반응 조회
     * @param user 사용자 객체
     * @param question 질문 객체
     * @return 반응 Optional 객체
     */
    Optional<Reaction> findByUserAndQuestion(User user, Question question);

    /**
     * 사용자가 특정 답변에 남긴 반응 조회
     * @param user 사용자 객체
     * @param answer 답변 객체
     * @return 반응 Optional 객체
     */
    Optional<Reaction> findByUserAndAnswer(User user, Answer answer);

    /**
     * 특정 질문에 대한 전체 반응 수 계산
     * @param question 질문 객체
     * @return 반응 수
     */
    int countByQuestion(Question question);

    /**
     * 특정 답변에 대한 전체 반응 수 계산
     * @param answer 답변 객체
     * @return 반응 수
     */
    int countByAnswer(Answer answer);

    /**
     * 사용자가 특정 질문에 반응했는지 확인
     * @param user 사용자 객체
     * @param question 질문 객체
     * @return 반응 존재 여부 (true/false)
     */
    boolean existsByUserAndQuestion(User user, Question question);

    /**
     * 사용자가 특정 답변에 반응했는지 확인
     * @param user 사용자 객체
     * @param answer 답변 객체
     * @return 반응 존재 여부 (true/false)
     */
    boolean existsByUserAndAnswer(User user, Answer answer);
}