package com.invy.backend.repository;

import com.invy.backend.entity.Answer;
import com.invy.backend.entity.Question;
import com.invy.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 답변(Answer) 엔티티에 접근하기 위한 repository 인터페이스
 * - 질문별 답변 조회 (채택 여부, 좋아요 수, 작성일 기준 정렬)
 * - 채택된 답변 조회
 * - 사용자별 답변 조회
 */
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    /**
     * 질문에 대한 모든 답변을 다음 순서로 정렬하여 조회
     * 1. 채택 여부 (채택된 답변 우선)
     * 2. 좋아요 수 (많은 순)
     * 3. 작성일 (최신순)
     *
     * @param question 질문 객체
     * @return 정렬된 답변 목록
     */
    List<Answer> findByQuestionOrderByIsSelectedDescLgtmCountDescCreatedAtDesc(Question question);

    /**
     * 질문에 대해 채택된 답변 조회
     * @param question 질문 객체
     * @return 채택된 답변 Optional 객체
     */
    Optional<Answer> findByQuestionAndIsSelectedTrue(Question question);

    /**
     * 특정 사용자가 작성한 모든 답변 조회
     * @param user 사용자 객체
     * @return 사용자가 작성한 답변 목록
     */
    List<Answer> findByUser(User user);
}