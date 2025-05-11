package roomescape.member.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.testFixture.Fixture.MEMBER_1;

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
import roomescape.DatabaseCleaner;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.testFixture.JdbcHelper;

@JdbcTest
@Import({JdbcMemberRepository.class, DatabaseCleaner.class})
@ActiveProfiles("test")
class JdbcMemberRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcMemberRepository jdbcMemberRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void clean() {
        databaseCleaner.clean();
    }

    private final static RowMapper<Member> MEMBER_ROW_MAPPER =
            (rs, rowNum) -> new Member(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    Role.valueOf(rs.getString("role"))
            );

    @DisplayName("유저를 저장할 수 있다.")
    @Test
    void save() {
        // given
        Member member = new Member( null, "test@example.com", "password", "멍구", Role.USER);

        // when
        jdbcMemberRepository.save(member);

        // then
        List<Member> members = jdbcTemplate.query("SELECT id, email, password, name, role FROM members", MEMBER_ROW_MAPPER);

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
        jdbcTemplate.update("INSERT INTO members (email, password, name, role) VALUES (?, ?, ?, ?)",
                email, password, name, Role.USER.name());

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

    @DisplayName("유저를 ID로 조회할 수 있다.")
    @Test
    void findMemberById() {
        // given
        Member member = MEMBER_1;
        JdbcHelper.insertMember(jdbcTemplate, member);

        // when
        Optional<Member> result = jdbcMemberRepository.findById(member.getId());

        // then
        assertThat(result).isPresent();
        Member resultMember = result.get();

        assertAll(
                () -> assertThat(resultMember.getPassword()).isEqualTo(member.getPassword()),
                () -> assertThat(resultMember.getName()).isEqualTo(member.getName())
        );
    }

    @DisplayName("유저를 ID로 조회했을 때 존재하지 않으면 빈 Optional을 반환한다.")
    @Test
    void findMemberById_emptyOptional() {
        // given
        Long nonExistingId = 999L;

        // when
        Optional<Member> result = jdbcMemberRepository.findById(nonExistingId);

        // then
        assertThat(result).isEmpty();
    }
}
