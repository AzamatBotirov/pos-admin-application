package com.mycompany.pos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.pos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarketBaseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarketBase.class);
        MarketBase marketBase1 = new MarketBase();
        marketBase1.setId(1L);
        MarketBase marketBase2 = new MarketBase();
        marketBase2.setId(marketBase1.getId());
        assertThat(marketBase1).isEqualTo(marketBase2);
        marketBase2.setId(2L);
        assertThat(marketBase1).isNotEqualTo(marketBase2);
        marketBase1.setId(null);
        assertThat(marketBase1).isNotEqualTo(marketBase2);
    }
}
