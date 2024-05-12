package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import roomescape.domain.Member;
import roomescape.domain.Role;

@JdbcTest
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class MemberDaoTest {
    private final JdbcTemplate jdbcTemplate;
    private final MemberDao memberDao;

    @Autowired
    MemberDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.memberDao = new MemberDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO member(name, email, password) VALUES ('켬미', 'aaa@naver.com', '1111')");
    }

    @DisplayName("id를 통해 사용자를 찾을 수 있다.")
    @Test
    void readMemberById() {
        Member member = memberDao.readMemberById(1L)
                .orElseThrow(() -> new IllegalArgumentException("해당 id에 멤버는 존재하지 않습니다."));

        Member expected = new Member(1L, "켬미", "aaa@naver.com", Role.MEMBER);
        assertThat(member).isEqualTo(expected);
    }

    @DisplayName("이메일과 비밀번호를 통해 사용자를 찾을 수 있다.")
    @Test
    void readMemberByEmailAndPassword() {
        Member member = memberDao.readMemberByEmailAndPassword("aaa@naver.com", "1111")
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일과 비밀번호인 멤버는 존재하지 않습니다."));

        Member expected = new Member(1L, "켬미", "aaa@naver.com", Role.MEMBER);
        assertThat(member).isEqualTo(expected);
    }

    @DisplayName("사용자를 추가할 수 있다.")
    @Test
    void createMember() {
        memberDao.createMember(new Member("호돌", "bbb@naver.com", Role.MEMBER), "2222");
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from member", Integer.class);

        assertThat(count).isEqualTo(2);
    }
}
