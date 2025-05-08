package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Member;

@JdbcTest
class JdbcMemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcMemberDao jdbcMemberDao;

    @BeforeEach
    void setUp() {
        jdbcMemberDao = new JdbcMemberDao(jdbcTemplate);
    }

    @Test
    @DisplayName("같은 이메일 사용자를 조회한다")
    void findMemberByEmail() {
        // given
        String email = "admin@email.com";

        // when
        Member result = jdbcMemberDao.findByEmail(email);

        // then
        assertThat(result)
                .extracting("id", "name", "email", "password")
                .containsExactly(1L, "테스트", "test@email.com", "password");
    }
}
