package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Member;
import roomescape.domain.Role;

@JdbcTest
class MemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE IF EXISTS member CASCADE;");
        jdbcTemplate.execute("""
            CREATE TABLE member
            (
                id   BIGINT       NOT NULL AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(50) NOT NULL DEFAULT 'USER',
                PRIMARY KEY (id),
                CHECK (role IN ('ADMIN', 'USER'))
            );
            """);

        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
            "두리", "duri@aa.com", "1234", "USER");
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
            "어드민", "admin@aa.com", "admin", "ADMIN");
    }

    @Test
    @DisplayName("이메일로 회원을 조회한다")
    void find_by_email_exist() {
        String email = "duri@aa.com";
        Optional<Member> foundMember = memberDao.findByEmail(email);
        assertThat(foundMember.isPresent()).isTrue();
        Assertions.assertAll(
            () -> assertThat(foundMember.get().getName()).isEqualTo("두리"),
            () -> assertThat(foundMember.get().getEmail()).isEqualTo(email),
            () -> assertThat(foundMember.get().getRole()).isEqualTo(Role.USER)
        );
    }

    @Test
    @DisplayName("이메일이 존재하지 않으면 빈 Optional을 반환한다")
    void find_by_email_not_exist() {
        String email = "asdasd";
        Optional<Member> foundMember = memberDao.findByEmail(email);

        assertThat(foundMember.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("ID로 회원을 조회한다")
    void find_by_id_exist() {
        Member memberToFind = memberDao.findByEmail("duri@aa.com").orElseThrow();
        Long memberId = memberToFind.getId();

        Optional<Member> foundMember = memberDao.findById(memberId);

        assertThat(foundMember.isPresent()).isTrue();
        Assertions.assertAll(
            () -> assertThat(foundMember.get().getId()).isEqualTo(memberId),
            () -> assertThat(foundMember.get().getEmail()).isEqualTo("duri@aa.com")
        );
    }

    @Test
    @DisplayName("ID가 존재하지 않으면 빈 Optional을 반환한다.")
    void find_by_id_not_exist() {
        Long memberId = 100L;

        Optional<Member> foundMember = memberDao.findById(memberId);

        assertTrue(foundMember.isEmpty());
    }

    @Test
    @DisplayName("모든 회원을 조회한다")
    void find_all_member() {
        List<Member> allMembers = memberDao.getAll();
        assertThat(allMembers).hasSize(2);
    }
}
