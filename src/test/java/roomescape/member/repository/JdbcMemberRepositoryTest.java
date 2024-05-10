package roomescape.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.fixture.Fixture;
import roomescape.member.model.Member;
import roomescape.member.repositoy.JdbcMemberRepository;

@JdbcTest
@Sql("/delete-data.sql")
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
    @DisplayName("이메일과 패스워드에 일치하는 회원이 있으면 반환")
    void findByEmailAndPassword() {
        memberRepository.save(Fixture.MEMBER_1);

        assertThat(memberRepository.findByEmailAndPassword(Fixture.MEMBER_1.getEmail(), Fixture.MEMBER_1.getPassword()))
                .isEqualTo(Optional.of(Fixture.MEMBER_1));
    }

    @Test
    @DisplayName("이메일과 패스워드에 일치하는 회원이 없으면 false 반환")
    void findByEmailAndPassword_NotExist() {
        assertThat(memberRepository.findByEmailAndPassword("없는 이메일", "틀린 패스워드"))
                .isNotPresent();
    }
}
