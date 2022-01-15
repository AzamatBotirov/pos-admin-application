package com.mycompany.pos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.pos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentList.class);
        PaymentList paymentList1 = new PaymentList();
        paymentList1.setId(1L);
        PaymentList paymentList2 = new PaymentList();
        paymentList2.setId(paymentList1.getId());
        assertThat(paymentList1).isEqualTo(paymentList2);
        paymentList2.setId(2L);
        assertThat(paymentList1).isNotEqualTo(paymentList2);
        paymentList1.setId(null);
        assertThat(paymentList1).isNotEqualTo(paymentList2);
    }
}
