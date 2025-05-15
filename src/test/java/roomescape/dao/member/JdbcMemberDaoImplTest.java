package roomescape.dao.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;

class JdbcMemberDaoImplTest {

    private DataSource datasource;
    private JdbcTemplate jdbcTemplate;
    private JdbcMemberDaoImpl memberDao;

    @BeforeEach
    void init() {
        datasource = new EmbeddedDatabaseBuilder()
                .setName("testdb-" + UUID.randomUUID())
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(datasource);
        memberDao = new JdbcMemberDaoImpl(jdbcTemplate);
    }

    @DisplayName("회원 저장에 성공하면 id를 반환한다.")
    @Test
    void save() {
        //given
        Member member = Member.fromWithoutId("testName", "testEmail", "1234");

        //when
        Long actual = memberDao.save(member);

        //then
        assertThat(actual).isEqualTo(1L);
    }

    @DisplayName("email을 통해서 회원을 찾을 수 있다.")
    @Test
    void findByEmail() {
        //given
        Member member = Member.fromWithoutId("testName", "testEmail", "1234");
        memberDao.save(member);

        //when
        Member actual = memberDao.findByEmail(member.getEmail())
                .orElse(null);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(Member.from(1L, "testName", "testEmail", "1234", MemberRole.USER));
    }

    @DisplayName("id를 통해서 회원을 찾을 수 있다.")
    @Test
    void findById() {
        //given
        Member member = Member.fromWithoutId("testName", "testEmail", "1234");
        Long id = memberDao.save(member);

        //when
        Member actual = memberDao.findById(id)
                .orElse(null);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(Member.from(1L, "testName", "testEmail", "1234", MemberRole.USER));
    }
}
