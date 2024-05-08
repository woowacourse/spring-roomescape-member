package roomescape.application.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.application.ServiceTest;
import roomescape.application.member.dto.request.MemberRegisterRequest;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;

@ServiceTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenManager tokenManager;

    @Test
    @DisplayName("중복된 이메일로 회원가입하는 경우, 예외가 발생한다.")
    void duplicatedEmailTest() {
        String email = "test@test.com";
        Member member = new Member("name", email, "12341234");
        memberRepository.save(member);
        MemberRegisterRequest request = new MemberRegisterRequest("hello", email, "12345678");

        assertThatCode(() -> memberService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 가입된 이메일입니다.");
    }

    @Test
    @DisplayName("회원가입을 통해 사용자가 생성된다.")
    void registerTest() {
        String email = "test@test.com";
        MemberRegisterRequest request = new MemberRegisterRequest("hello", email, "12341234");
        memberService.register(request);
        Optional<Member> actual = memberRepository.findByEmail(email);
        assertThat(actual).isPresent();
    }
}
