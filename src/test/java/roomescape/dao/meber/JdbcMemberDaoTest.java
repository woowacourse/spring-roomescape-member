package roomescape.dao.meber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Member;

@JdbcTest
@Import(JdbcMemberDao.class)
@Sql({"/test-schema.sql", "/test-member-data.sql"})
class JdbcMemberDaoTest {

    @Autowired
    private JdbcMemberDao jdbcMemberDao;

    @Test
    void findByEmailTest() {

        // when
        final Optional<Member> member = jdbcMemberDao.findByEmail("cheche903@naver.com");

        // then
        assertAll(() -> {
            assertThat(member.isPresent()).isTrue();
            assertThat(member.get().getName()).isEqualTo("체체");
        });
    }
}
