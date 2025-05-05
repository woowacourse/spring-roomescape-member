package roomescape.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ActiveProfiles;
import roomescape.reservation.domain.Member;
import roomescape.reservation.infrastructure.JdbcMemberRepository;

@JdbcTest
@Import(JdbcMemberRepository.class)
@ActiveProfiles("test")
class JdbcMemberRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcMemberRepository jdbcMemberRepository;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE theme");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE members");
        jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private final static RowMapper<Member> MEMBER_ROW_MAPPER =
            (rs, rowNum) -> new Member(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name")
            );

    @DisplayName("유저를 저장할 수 있다.")
    @Test
    void save() {
        // given
        Member member = new Member( null, "test@example.com", "password", "멍구");

        // when
        jdbcMemberRepository.save(member);

        // then
        List<Member> members = jdbcTemplate.query("SELECT id, email, password, name FROM members", MEMBER_ROW_MAPPER);

        assertThat(members).hasSize(1);
        assertThat(members.getFirst().getEmail()).isEqualTo(member.getEmail());
    }

    @DisplayName("유저를 이메일로 조회할 수 있다.")
    @Test
    void findMemberByEmail() {
        // given
        String email = "test@example.com";
        String password = "password";
        String name = "멍구";
        jdbcTemplate.update("INSERT INTO members (email, password, name) VALUES (?, ?, ?)",
                email, password, name);

        // when
        Optional<Member> result = jdbcMemberRepository.findByEmail(email);

        // then
        assertThat(result).isPresent();
        Member member = result.get();

        assertAll(
                () -> assertThat(member.getPassword()).isEqualTo(password),
                () -> assertThat(member.getName()).isEqualTo(name)
        );
    }
}
