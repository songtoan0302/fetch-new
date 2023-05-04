package com.vnpt.tnvn.send_email.repo;

import com.vnpt.tnvn.send_email.model.OracleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OracleUserRepository extends JpaRepository<OracleUser, Long> {
    OracleUser findOracleUserById(long id);
}
