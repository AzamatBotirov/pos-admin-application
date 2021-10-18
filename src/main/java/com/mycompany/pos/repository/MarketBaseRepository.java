package com.mycompany.pos.repository;

import com.mycompany.pos.domain.MarketBase;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MarketBase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarketBaseRepository extends JpaRepository<MarketBase, Long> {}
