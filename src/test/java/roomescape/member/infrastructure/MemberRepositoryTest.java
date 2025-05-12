package roomescape.member.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.member.domain.Member;

@JdbcTest
@Import(MemberRepository.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이메일과 비밀번호로 회원을 조회할 수 있다")
    void test1() {
        // given
        String email = "mimi@email.com";
        String password = "password";

        // when
        Optional<Member> result = memberRepository.findByEmailAndPassword(email, password);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("미미");
    }

    @Test
    @DisplayName("존재하지 않는 이메일과 비밀번호로 조회 시 빈 Optional을 반환한다")
    void test2() {
        // given
        String email = "wrong@email.com";
        String password = "wrong";

        // when
        Optional<Member> result = memberRepository.findByEmailAndPassword(email, password);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("id로 회원을 조회할 수 있다")
    void test3() {
        // given
        Long mimiId = 1L;

        // when
        Optional<Member> result = memberRepository.findById(mimiId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo("mimi@email.com");
    }

    @Test
    @DisplayName("존재하지 않는 id로 조회 시 빈 Optional을 반환한다")
    void test4() {
        // given
        Long notExistedId = 999L;

        // when
        Optional<Member> result = memberRepository.findById(notExistedId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("모든 회원을 조회할 수 있다")
    void test5() {
        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(members).hasSize(3);
        assertThat(members.get(0).name()).isEqualTo("미미");
        assertThat(members.get(1).name()).isEqualTo("노랑");
        assertThat(members.get(2).name()).isEqualTo("어드민");
    }
}
