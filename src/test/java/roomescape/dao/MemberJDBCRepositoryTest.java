package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.Member;

@JdbcTest
@Sql(scripts = "/test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class MemberJDBCRepositoryTest {
    private MemberRepository memberRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        memberRepository = new MemberJDBCRepository(jdbcTemplate);
    }

    @DisplayName("이메일로 회원을 조회한다. - 성공")
    @Test
    void findMemberByEmailTest() {
        //when
        Member member = memberRepository.findByEmail("admin@email.com").get();

        //then
        assertThat(member.getId()).isEqualTo(1);
    }

    @DisplayName("이메일로 회원을 조회한다. - 실패")
    @Test
    void NotfoundMemberByEmailTest() {
        assertThat(memberRepository.findByEmail("lily12@email.com")).isEqualTo(Optional.empty());
    }

    @DisplayName("id로 회원을 조회한다. - 성공")
    @Test
    void findMemberByIdTest() {
        //when
        Member member = memberRepository.findById(1).get();

        //then
        assertThat(member.getId()).isEqualTo(1);
    }

    @DisplayName("id로 회원을 조회한다. - 실패")
    @Test
    void NotfoundMemberByIdTest() {
        assertThat(memberRepository.findById(5)).isEqualTo(Optional.empty());
    }
}
