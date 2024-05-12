package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;

@JdbcTest
@Sql("/member.sql")
class JdbcMemberRepositoryTest {
    private final MemberRepository memberRepository;

    @Autowired
    JdbcMemberRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.memberRepository = new JdbcMemberRepository(jdbcTemplate);
    }

    @Test
    void 이메일로_유저를_찾는다() {
        Member member = memberRepository.findByEmail("prin@gmail.com").orElseThrow();

        assertThat(member.getName()).isEqualTo("프린");
    }
}
