package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.business.model.entity.Member;
import roomescape.member.business.model.entity.Role;
import roomescape.member.business.model.repository.MemberDao;
import roomescape.member.infrastructure.MemberDaoImpl;

@JdbcTest
@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberDaoImplTest {

    private MemberDao memberDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDaoImpl(jdbcTemplate);
    }

    @Test
    void 이메일과_비밀번호로_회원을_조회한다() {
        // given
        String email = "test@test.com";
        String password = "1234";
        // when

        // then
        assertThat(memberDao.findByEmailAndPassword(email, password).get()).isEqualTo(
                new Member(1L, "", "", "", Role.MEMBER)
        );

    }
}
