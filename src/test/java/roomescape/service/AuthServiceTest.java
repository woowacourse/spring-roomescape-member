package roomescape.service;

import java.util.EnumSet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.domain.repository.MemberRepository;
import roomescape.exception.member.AuthenticationFailureException;
import roomescape.service.security.JwtProvider;
import roomescape.web.dto.request.member.LoginRequest;

@SpringBootTest
class AuthServiceTest {
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @ParameterizedTest
    @EnumSource(Role.class)
    @DisplayName("토큰을 통해 올바른 권한 안에 있는지 확인 할 수 있다")
    void verifyPermission_ShouldCheckValidPermissionRole(Role role) {
        // given
        String token = jwtProvider.encode(new Member(1L, "name", "e@e.e", "p", role));

        // when
        boolean result = authService.verifyPermission(token, EnumSet.of(role));

        // then
        Assertions.assertThat(result).isTrue();
    }


    @Test
    @DisplayName("이메일과 비밀번호로 로그인 기능을 제공한다")
    void login_ShouldProvideLoginFeature() {
        // given
        Member member = new Member("name", "hello", "password");
        LoginRequest request = new LoginRequest("hello", "password");
        Member savedMember = memberRepository.save(member);

        // when
        String token = authService.login(request);

        // then
        Assertions.assertThat(jwtProvider.extractId(token)).isEqualTo(savedMember.getId());
    }

    @Test
    @DisplayName("이메일이 없는 정보라면 로그인 중 예외를 발생시킨다")
    void login_ShouldFailed_WhenEmailDoesNotExist() {
        // given
        LoginRequest request = new LoginRequest("hello", "password");

        // when & then
        Assertions.assertThatThrownBy(() -> authService.login(request))
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
        Assertions.assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(AuthenticationFailureException.class);
    }
}
