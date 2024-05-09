package roomescape.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import roomescape.domain.Member;
import roomescape.domain.repository.MemberRepository;
import roomescape.exception.member.AuthenticationFailureException;
import roomescape.service.security.JwtUtils;
import roomescape.web.dto.request.LoginRequest;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일과 비밀번호로 로그인 기능을 제공한다")
    void login_ShouldProvideLoginFeature() {
        // given
        Member member = new Member("name", "hello", "password");
        LoginRequest request = new LoginRequest("hello", "password");
        Member savedMember = memberRepository.save(member);

        // when
        String token = memberService.login(request);

        // then
        Assertions.assertThat(JwtUtils.decode(token)).isEqualTo(savedMember.getId());
    }

    @Test
    @DisplayName("이메일이 없는 정보라면 로그인 중 예외를 발생시킨다")
    void login_ShouldFailed_WhenEmailDoesNotExist() {
        // given
        LoginRequest request = new LoginRequest("hello", "password");

        // when & then
        Assertions.assertThatThrownBy(() -> memberService.login(request))
                .isInstanceOf(AuthenticationFailureException.class);
    }

    @Test
    @DisplayName("비밀번호가 틀리면 로그인 중 예외를 발생시킨다")
    void login_ShouldFailed_WhenInvalidLoginInfo() {
        // given
        Member member = new Member("name", "hello", "password");
        LoginRequest request = new LoginRequest("hello", "world");
        memberRepository.save(member);

        // when & then
        Assertions.assertThatThrownBy(() -> memberService.login(request))
                .isInstanceOf(AuthenticationFailureException.class);
    }

    @Test
    @DisplayName("유효한 토큰인지 확인할 수 있다")
    void validateToken_ShouldVerifyToken() {
        // given
        Member member = new Member("name", "hello", "password");
        LoginRequest request = new LoginRequest("hello", "password");
        memberRepository.save(member);
        String token = memberService.login(request);

        // when & then
        Assertions.assertThatCode(() -> memberService.findMemberByToken(token))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("유효하지 않는 토큰인지 확인할 수 있다")
    void validateToken_ShouldThrowException_WhenTokenIsNotValid() {
        Assertions.assertThatCode(() -> memberService.findMemberByToken("hello, world"))
                .isInstanceOf(AuthenticationFailureException.class);
    }
}
