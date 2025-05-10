package roomescape.dao.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Member;

@JdbcTest
@Import(JdbcMemberDao.class)
@Sql("/schema.sql")
class JdbcMemberDaoTest {

    @Autowired
    private JdbcMemberDao jdbcMemberDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void findByEmailTest() {

        // given
        final String sql = """
                INSERT INTO member(name, email, password, role)
                VALUES ('체체', 'cheche903@naver.com', 'password1234', 'USER')
                """;
        jdbcTemplate.update(sql);

        // when
        final Optional<Member> member = jdbcMemberDao.findByEmail("cheche903@naver.com");

        // then
        assertAll(() -> {
            assertThat(member.isPresent()).isTrue();
            assertThat(member.get().getName()).isEqualTo("체체");
        });
    }

    @Test
    void findAllTest() {

        // given
        final String sql = """
                INSERT INTO member(name, email, password, role)
                VALUES ('체체', 'cheche903@naver.com', 'password1234', 'USER')
                """;
        jdbcTemplate.update(sql);

        // when
        final List<Member> members = jdbcMemberDao.findAll();

        // then
        assertThat(members.size()).isEqualTo(1);
    }
}
