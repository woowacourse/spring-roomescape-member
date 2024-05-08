package roomescape.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.user.Member;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberDaoTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    MemberDao memberDao;

    @Test
    void create_member() {
        final var member = memberDao.create(
                Member.from(null, "조이썬", "i894@naver.com", "password1234"));

        final var result = memberDao.findById(member.getId())
                .orElseThrow();

        assertThat(result).isEqualTo(member);
    }

    @Test
    @DisplayName("이메일과 비밀번호를 통해 사용자를 찾는다.")
    void find_member_with_email_and_password() {
        final var member = memberDao.create(
                Member.from(null, "조이썬", "i894@naver.com", "password1234"));

        final var result = memberDao.findByEmailAndPassword("i894@naver.com", "password1234")
                .orElseThrow();

        assertThat(result).isEqualTo(member);
    }


}
