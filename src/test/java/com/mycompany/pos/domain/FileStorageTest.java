package com.mycompany.pos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.pos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FileStorageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileStorage.class);
        FileStorage fileStorage1 = new FileStorage();
        fileStorage1.setId(1L);
        FileStorage fileStorage2 = new FileStorage();
        fileStorage2.setId(fileStorage1.getId());
        assertThat(fileStorage1).isEqualTo(fileStorage2);
        fileStorage2.setId(2L);
        assertThat(fileStorage1).isNotEqualTo(fileStorage2);
        fileStorage1.setId(null);
        assertThat(fileStorage1).isNotEqualTo(fileStorage2);
    }
}
