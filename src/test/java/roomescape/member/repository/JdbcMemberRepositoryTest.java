package roomescape.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.fixture.Fixture;
import roomescape.member.model.Member;
import roomescape.member.repositoy.JdbcMemberRepository;

@JdbcTest
public class JdbcMemberRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    JdbcMemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new JdbcMemberRepository(jdbcTemplate, jdbcTemplate.getDataSource());
    }

    @Test
    @DisplayName("회원 저장")
    void save() {
        Member member = new Member(null, "mark", "email@woowa.com", "password");

        Member saved = memberRepository.save(member);

        assertThat(saved).isEqualTo(new Member(saved.getId(), "mark", "email@woowa.com", "password"));
    }

    @Test
    @DisplayName("이메일로 회원 조회")
    void findByEmail() {
        memberRepository.save(Fixture.MEMBER_1);

        assertThat(memberRepository.findByEmail(Fixture.MEMBER_1.getEmail()))
                .isEqualTo(Optional.of(Fixture.MEMBER_1));
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 회원 조회하면 빈 Optional 반환")
    void findByEmailNotExist() {
        assertThat(memberRepository.findByEmail("asdqweaxcqsd@asd.com"))
                .isEqualTo(Optional.empty());
    }
}
