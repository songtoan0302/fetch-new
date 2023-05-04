package com.vnpt.tnvn.send_email.repo;

import com.vnpt.tnvn.send_email.model.OracleQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OracleQuestionRepository extends JpaRepository<OracleQuestion, Long> {
    List<OracleQuestion> findAllByIsAnswerIsNotLikeAndIsNotifiedEquals(int isAnswer, int isNotified);
}
