package com.blitz.account.repository;

import com.blitz.account.domain.Journal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Journal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {}
