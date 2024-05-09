package roomescape.member.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.domain.Member;
import roomescape.theme.dao.ThemeDao;

@JdbcTest
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void findMembersTest() {
        jdbcTemplate.update("INSERT INTO member (email, name) VALUES (?, ?)", "bri@abc.com", "브리");
        jdbcTemplate.update("INSERT INTO member (email, name) VALUES (?, ?)", "brown@abc.com", "브라운");
        List<Member> expected = List.of(
                new Member(1L, "브리", "bri@abc.com"),
                new Member(2L, "브라운", "brown@abc.com"));

        List<Member> actual = memberDao.findMembers();

        assertThat(actual).isEqualTo(expected);
    }
}
