package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.entity.Member;
import roomescape.entity.Role;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class H2MemberDaoTest {

    private MemberDao memberDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        memberDao = new H2MemberDao(jdbcTemplate);
    }

    @Test
    void 이메일과_비밀번호로_회원_조회() {
        Optional<Member> member = memberDao.findByEmailAndPassword("admin@email.com", "password");

        assertAll(
            () -> assertThat(member).isPresent(),
            () -> assertThat(member.get().getName()).isEqualTo("어드민"),
            () -> assertThat(member.get().getRole()).isEqualTo(Role.ADMIN)
        );
    }

    @Test
    void 잘못된_이메일_또는_비밀번호로_회원_조회시_빈값_반환() {
        Optional<Member> member = memberDao.findByEmailAndPassword("wrong@email.com", "wrongpass");

        assertThat(member).isEmpty();
    }

    @Test
    void ID로_회원_조회() {
        Optional<Member> member = memberDao.findById(1L);

        assertAll(
            () -> assertThat(member).isPresent(),
            () -> assertThat(member.get().getEmail()).isEqualTo("admin@email.com")
        );

    }

    @Test
    void 존재하지_않는_ID로_조회시_빈값_반환() {
        Optional<Member> member = memberDao.findById(999L);

        assertThat(member).isEmpty();
    }

    @Test
    void 모든_회원_조회() {
        List<Member> members = memberDao.findAll();

        assertAll(
            () -> assertThat(members).hasSize(2),
            () -> assertThat(members.get(0).getName()).isEqualTo("어드민"),
            () -> assertThat(members.get(1).getName()).isEqualTo("브라운")
        );
    }
}