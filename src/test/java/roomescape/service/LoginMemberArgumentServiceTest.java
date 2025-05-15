package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.exception.NotFoundException;
import roomescape.repository.MemberRepository;
import roomescape.test.fake.FakeH2MemberRepository;

public class LoginMemberArgumentServiceTest {

    private final MemberRepository memberRepository = new FakeH2MemberRepository();
    private final AuthService authService = new AuthService();
    private final MemberService memberService = new MemberService(memberRepository, authService);


    @DisplayName("아이디와 이메일로 사용자를 가져오는지 확인합니다.")
    @Test
    void findMemberWithEmailAndPasswordTest() {
        Member expectedMember = new Member(1L, "아마", "email@email.com", "password", "member");
        LoginRequest loginRequest = new LoginRequest("password", "email@email.com");

        Member member = memberService.findMemberWithEmailAndPassword(loginRequest);

        assertThat(member).isEqualTo(expectedMember);
    }

    @DisplayName("잘못된 아이디와 이메일로 유저를 찾으면 예외가 발생합니다.")
    @Test
    void findMemberWithEmailAndPasswordErrorTest() {
        LoginRequest loginRequest = new LoginRequest("passwrd", "email@email.com");

        assertThatThrownBy(() -> memberService.findMemberWithEmailAndPassword(loginRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] 사용자 정보를 가져오지 못했습니다.");
    }
}
