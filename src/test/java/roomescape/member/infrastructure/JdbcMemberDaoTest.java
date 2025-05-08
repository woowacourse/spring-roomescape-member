package roomescape.member.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.business.domain.Member;
import roomescape.member.business.domain.Role;
import roomescape.member.business.repository.MemberDao;

@JdbcTest
@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcMemberDaoTest {

    private MemberDao memberDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        memberDao = new JdbcMemberDao(jdbcTemplate);
    }

    @Test
    void 이메일과_비밀번호로_회원을_조회한다() {
        // given
        String email = "test1@test.com";
        String password = "1234";
        // when

        // then
        assertThat(memberDao.findByEmailAndPassword(email, password).get()).isEqualTo(
                new Member(1L, "", "", "", Role.MEMBER)
        );

    }
}
