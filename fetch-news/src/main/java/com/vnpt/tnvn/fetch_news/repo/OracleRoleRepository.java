package com.vnpt.tnvn.fetch_news.repo;

import com.vnpt.tnvn.fetch_news.model.oracle.OracleRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OracleRoleRepository extends JpaRepository<OracleRole, Long> {
    OracleRole findOracleRoleById(long id);
}