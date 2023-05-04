package com.vnpt.tnvn.send_email.repo;

import com.vnpt.tnvn.send_email.model.OracleUserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OracleUserCategoryRepository extends JpaRepository<OracleUserCategory, Long> {
    List<OracleUserCategory> findOracleUserCategoriesByEmbedded_CategoryId(long categoryId);
}
