package com.blitz.account.repository;

import com.blitz.account.domain.JournalLine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the JournalLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JournalLineRepository extends JpaRepository<JournalLine, Long> {}
