package roomescape.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberName;

@JdbcTest
@Import(MemberRepository.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("id로 회원을 찾는다.")
    @Test
    void findByIdTest() {
        Member member = new Member(new MemberName("카키"), "kaki@email.com", "1234");
        Long memberId = memberRepository.save(member);
        Member findMember = memberRepository.findById(memberId).get();

        assertThat(findMember.getId()).isEqualTo(memberId);
    }

    @DisplayName("테이블에 존재하는 이름 또는 이메일인지 확인한다.")
    @ParameterizedTest
    @CsvSource({"'카키', 'test@email.com', true", "'테스트', 'kaki@email.com', true", "'테스트', 'test@email.com', false"})
    void existNameOrEmailTest(String name, String email, boolean exist) {
        Member member = new Member(new MemberName("카키"), "kaki@email.com", "1234");
        memberRepository.save(member);

        Member newMember = new Member(new MemberName(name), email, "1234");
        boolean existNameOrEmail = memberRepository.existNameOrEmail(newMember);

        assertThat(existNameOrEmail).isEqualTo(exist);
    }
}
