package roomescape.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Role;

@JdbcTest
@Sql(scripts = "/member_test_data.sql")
class MemberRepositoryTest {

    private MemberRepository memberRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        memberRepository = new MemberRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("이메일과 비밀번호에 해당되는 회원 정보를 가져온다.")
    void findByEmailAndPassword() {
        // given
        Member targetMember = new Member(1L, new Name("user1"), "user1@gmail.com", "user1", Role.USER);

        // when
        Member findMember = memberRepository.findByEmailAndPassword(targetMember.getEmail(), targetMember.getPassword())
                .orElseThrow();

        // then
        assertThat(findMember).isEqualTo(targetMember);
    }
}
