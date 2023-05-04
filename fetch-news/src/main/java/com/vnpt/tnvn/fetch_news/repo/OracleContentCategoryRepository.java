package com.vnpt.tnvn.fetch_news.repo;

import com.vnpt.tnvn.fetch_news.model.oracle.OracleContentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OracleContentCategoryRepository extends JpaRepository<OracleContentCategory, Long> {
    List<OracleContentCategory> findOracleContentCategoriesByContentId(long contentId);
}