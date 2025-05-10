package roomescape.member.infrastructure.dao;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.domain.Member;

@JdbcTest
class MemberDaoTest {

    private final MemberDao memberDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    MemberDaoTest(JdbcTemplate jdbcTemplate) {
        this.memberDao = new MemberDao(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void resetAutoIncrement() {
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    @DisplayName("이메일을 사용한 사용자 조회 테스트")
    void findByEmail() {
        // given
        String email = "email@email.com";
        String initQuery = "INSERT INTO member (name, email, password, role) "
                + "VALUES ('name', 'email@email.com', 'password', 'USER')";
        jdbcTemplate.update(initQuery);

        // when
        Optional<Member> user = memberDao.findByEmail(email);

        // then
        Assertions.assertThat(user.get().getName()).isEqualTo("name");
    }
}
