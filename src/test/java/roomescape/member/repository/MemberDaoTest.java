package roomescape.member.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;
import roomescape.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("사용자 DAO")
@Sql(value = {"/recreate_table.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@JdbcTest
class MemberDaoTest {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberDaoTest(JdbcTemplate jdbcTemplate) {
        this.memberRepository = new MemberDao(jdbcTemplate);
    }

    @DisplayName("사용자 DAO는 email에 일치하는 사용자를 반환한다.")
    @Test
    void findUserByEmail() {
        // given
        String email = "test@gmail.com";

        // when
        Optional<Member> actual = memberRepository.findByEmail(email);

        // then
        assertThat(actual).isPresent();
    }
}
