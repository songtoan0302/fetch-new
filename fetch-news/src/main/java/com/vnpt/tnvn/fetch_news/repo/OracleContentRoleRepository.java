package com.vnpt.tnvn.fetch_news.repo;

import com.vnpt.tnvn.fetch_news.model.oracle.OracleContentRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OracleContentRoleRepository extends JpaRepository<OracleContentRole, Long> {
    List<OracleContentRole> findOracleContentRolesByEmbeddedId_ContentId(long contentId);
}