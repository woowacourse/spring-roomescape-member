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

    @DisplayName("특정 권한에 해당하는 회원들을 조회할 수 있다")
    @Test
    void canFindAll() {
        // given
        template.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "관리자1", "admin1@test.com", "ecxewqe!23", MemberRole.ADMIN.toString());
        template.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원1", "test1@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());
        template.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원2", "test2@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());

        // when & then
        assertAll(
                () -> assertThat(memberRepository.findAllByRole(MemberRole.ADMIN)).hasSize(1),
                () -> assertThat(memberRepository.findAllByRole(MemberRole.GENERAL)).hasSize(2)
        );
    }

    @DisplayName("id로 회원을 조회할 수 있다")
    @Test
    void canFindById() {
        // given
        template.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원", "test@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());

        // when
        Optional<Member> actualMember = memberRepository.findById(1L);

        // then
        Member expectedMember = new Member(1L, "회원", "test@test.com", "ecxewqe!23", MemberRole.GENERAL);
        assertAll(
                () -> assertThat(actualMember.isPresent()).isTrue(),
                () -> assertThat(actualMember.get()).isEqualTo(expectedMember)
        );
    }

    @DisplayName("이메일로 회원을 조회할 수 있다")
    @Test
    void canFindByEmail() {
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

    @DisplayName("회원을 추가할 수 있다")
    @Test
    void testMethodNameHere() {
        // given
        Member member = Member.createWithoutId("member1", "test@test.com", "asdf1234!", MemberRole.GENERAL);

        // when
        long savedId = memberRepository.add(member);

        // then
        Optional<Member> savedMember = memberRepository.findById(savedId);
        Member expectedMember = new Member(
                1L, member.getName(), member.getEmail(), member.getPassword(), member.getRole());
        assertAll(
                () -> assertThat(savedMember.isPresent()).isTrue(),
                () -> assertThat(savedMember.get()).isEqualTo(expectedMember)
        );
    }
}
