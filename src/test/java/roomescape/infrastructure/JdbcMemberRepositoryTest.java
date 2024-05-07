package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.member.Member;

@JdbcTest
@Import(JdbcMemberRepository.class)
class JdbcMemberRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcMemberRepository jdbcMemberRepository;

    @DisplayName("회원을 저장한다.")
    @Test
    void saveTest() {
        Member member = new Member("name", "email@test.com", "password");
        jdbcMemberRepository.save(member);
        int totalRowCount = getTotalRowCount();
        assertThat(totalRowCount).isEqualTo(1);
    }

    @DisplayName("이메일로 회원이 존재하는지 확인한다.")
    @Test
    void existsByEmailTest() {
        jdbcTemplate.execute("insert into member (name, email, password) values ('name', 'email@test.com', 'password')");
        boolean existsByEmail = jdbcMemberRepository.existsByEmail("email@test.com");
        assertThat(existsByEmail).isTrue();
    }

    private int getTotalRowCount() {
        String sql = "select count(*) from member";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
