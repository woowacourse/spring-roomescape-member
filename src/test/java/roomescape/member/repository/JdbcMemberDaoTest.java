package roomescape.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.domain.Member;
import roomescape.member.domain.enums.Role;
import roomescape.util.repository.TestDataSourceFactory;

class JdbcMemberDaoTest {

    private JdbcMemberDao jdbcMemberDao;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        DataSource dataSource = TestDataSourceFactory.getEmbeddedDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcMemberDao = new JdbcMemberDao(jdbcTemplate);
    }

    @AfterEach
    void dropTable() {
        String dropSql = "DROP TABLE IF EXISTS reservation, reservation_time, theme, member";
        jdbcTemplate.execute(dropSql);
    }

    @DisplayName("사용자 목록을 조회한다")
    @Test
    void find_all_test() {
        // when
        List<Member> members = jdbcMemberDao.findAll();

        // then
        assertThat(members).extracting(Member::getEmail)
                .containsExactlyInAnyOrder(
                        "rookie@woowa.com",
                        "haru@woowa.com",
                        "verus@woowa.com",
                        "user@user.com",
                        "admin@admin.com"
                );
        assertThat(members).extracting(Member::getName)
                .containsExactlyInAnyOrder(
                        "루키",
                        "하루",
                        "베루스",
                        "사용자",
                        "관리자"
                );
    }

    @DisplayName("email에 해당하는 사용자를 조회한다")
    @CsvSource({
            "rookie@woowa.com, 1, 루키, USER",
            "haru@woowa.com, 2, 하루, USER",
            "verus@woowa.com, 3, 베루스, ADMIN",
            "user@user.com, 4, 사용자, USER",
            "admin@admin.com, 5, 관리자, ADMIN"
    })
    @ParameterizedTest
    void find_by_email_test(String email, Long id, String name, Role role) {
        // when
        Member findMember = jdbcMemberDao.findByEmail(email).get();

        // then
        assertAll(
                () -> assertThat(findMember.getId()).isEqualTo(id),
                () -> assertThat(findMember.getName()).isEqualTo(name),
                () -> assertThat(findMember.getRole()).isEqualTo(role)
        );
    }

    @DisplayName("email에 해당하는 사용자를 조회 시 해당하는 사용자가 없으면 빈 객체를 반환한다")
    @Test
    void find_by_email_empty_test() {
        // given
        String email = "invalidEmail";

        // when
        Optional<Member> findMember = jdbcMemberDao.findByEmail(email);

        // then
        assertThat(findMember).isEmpty();
    }

    @DisplayName("ID에 해당하는 사용자를 조회한다")
    @CsvSource({
            "1, rookie@woowa.com, 루키, USER",
            "2, haru@woowa.com, 하루, USER",
            "3, verus@woowa.com, 베루스, ADMIN",
            "4, user@user.com, 사용자, USER",
            "5, admin@admin.com, 관리자, ADMIN"
    })
    @ParameterizedTest
    void find_by_id_test(Long id, String email, String name, Role role) {
        /// when
        Member findMember = jdbcMemberDao.findById(id).get();

        // then
        assertAll(
                () -> assertThat(findMember.getEmail()).isEqualTo(email),
                () -> assertThat(findMember.getName()).isEqualTo(name),
                () -> assertThat(findMember.getRole()).isEqualTo(role)
        );
    }

    @DisplayName("id에 해당하는 사용자를 조회 시 해당하는 사용자가 없으면 빈 객체를 반환한다")
    @Test
    void find_by_id_empty_test() {
        // given
        Long id = 100L;

        // when
        Optional<Member> findMember = jdbcMemberDao.findById(id);

        // then
        assertThat(findMember).isEmpty();
    }
}
