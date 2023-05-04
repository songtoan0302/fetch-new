package com.vnpt.tnvn.fetch_news.repo;

import com.vnpt.tnvn.fetch_news.model.oracle.OracleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OracleCategoryRepository extends JpaRepository<OracleCategory, Long> {
    OracleCategory findOracleCategoryById(long id);
}