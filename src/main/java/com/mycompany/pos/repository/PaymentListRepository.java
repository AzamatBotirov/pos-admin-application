package com.mycompany.pos.repository;

import com.mycompany.pos.domain.PaymentList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PaymentList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentListRepository extends JpaRepository<PaymentList, Long> {}
