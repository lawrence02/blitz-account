package com.blitz.account.repository;

import com.blitz.account.domain.ChartOfAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChartOfAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChartOfAccountRepository extends JpaRepository<ChartOfAccount, Long>, JpaSpecificationExecutor<ChartOfAccount> {}
