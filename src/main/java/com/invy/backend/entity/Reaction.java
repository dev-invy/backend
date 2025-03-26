package com.invy.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 사용자가 질문이나 답변에 LGTM(좋아요) 반응을 남긴 정보를 저장하는 엔티티 클래스
 * - 질문 또는 답변 중 하나에만 연결됨 (둘 다 연결되지 않음)
 * - 사용자 당 질문/답변 별로 최대 1개의 반응만 가능
 */
@Entity
@Table(name = "reactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    /**
     * 질문과 답변 중 하나만 연결되어야 함을 검증하는 메서드
     * 엔티티 저장/수정 전에 자동으로 호출됨
     */
    @PrePersist
    @PreUpdate
    private void validateReaction() {
        if ((question == null && answer == null) || (question != null && answer != null)) {
            throw new IllegalStateException("A reaction must be associated with either a question or an answer, not both.");
        }
    }

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}