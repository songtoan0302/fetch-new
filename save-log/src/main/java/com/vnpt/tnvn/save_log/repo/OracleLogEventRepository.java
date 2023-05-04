package com.vnpt.tnvn.save_log.repo;

import com.vnpt.tnvn.save_log.model.OracleLogEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OracleLogEventRepository extends JpaRepository<OracleLogEvent, Long> {
}
