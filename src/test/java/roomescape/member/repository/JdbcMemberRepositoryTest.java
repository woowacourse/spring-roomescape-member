package roomescape.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.domain.Member;
import roomescape.util.MemberFixture;

@JdbcTest
class JdbcMemberRepositoryTest {

    private final JdbcMemberRepository jdbcMemberRepository;

    @Autowired
    JdbcMemberRepositoryTest(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcMemberRepository = new JdbcMemberRepository(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("이메일을 통해 회원을 조회한다.")
    void findByEmail() {
        // given
        String email = "email@sdf.com";
        Member member = jdbcMemberRepository.save(MemberFixture.getOne(email));

        // when
        assertThat(jdbcMemberRepository.findByEmail(email)).isEqualTo(Optional.of(member));
    }

    @Test
    @DisplayName("존재하지 않은 회원인 경우, 빈 옵셔널을 반환한다.")
    void findByEmail_WhenNotExistsMember() {
        // given
        String email = "email@sdf.com";

        // when
        assertThat(jdbcMemberRepository.findByEmail(email)).isEmpty();
    }
}
