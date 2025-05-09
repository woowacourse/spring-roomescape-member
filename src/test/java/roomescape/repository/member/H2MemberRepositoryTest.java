package roomescape.repository.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;

@JdbcTest
@Import(H2MemberRepository.class)
class H2MemberRepositoryTest {

    @Autowired
    private JdbcTemplate template;
    @Autowired
    private H2MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        template.execute("ALTER TABLE member ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("모든 예약을 조회할 수 있다")
    @Test
    void findByEmail() {
        // given
        String email = "test@test.com";
        template.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원", email, "ecxewqe!23", MemberRole.GENERAL.toString());

        // when
        Optional<Member> member = memberRepository.findByEmail(email);

        // then
        assertAll(
                () -> assertThat(member.isPresent()).isTrue(),
                () -> assertThat(member.get())
                        .isEqualTo(new Member(1L, "회원", email, "ecxewqe!23", MemberRole.GENERAL))
        );
    }
}
