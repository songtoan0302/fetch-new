package com.vnpt.tnvn.fetch_news.repo;

import com.vnpt.tnvn.fetch_news.model.oracle.OracleContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OracleContentRepository extends JpaRepository<OracleContent, Long> {
    List<OracleContent> findOracleContentsByTitle(String title);
    List<OracleContent> findOracleContentsBySourceAndOriginalId(String source, String originalId);
}