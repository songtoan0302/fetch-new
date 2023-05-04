package com.vnpt.tnvn.send_email.repo;

import com.vnpt.tnvn.send_email.model.OracleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OracleCategoryRepository extends JpaRepository<OracleCategory, Long> {
    OracleCategory findOracleCategoryById(long id);
}