package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import roomescape.domain.user.Role;
import roomescape.service.MemberService;
import roomescape.service.dto.output.TokenLoginOutput;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class CheckAdminInterceptorTest {
    @MockBean
    MemberService memberService;
    CheckAdminInterceptor sut;
    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        sut = new CheckAdminInterceptor(memberService);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

    }

    @Test
    @DisplayName("토큰의 정보가 관리자이면 참을 반환한다.")
    void return_true_when_token_info_is_admin() {
        final var output = new TokenLoginOutput(1, "운영자", "admin@email.com", "password1234", Role.ADMIN.getValue());
        Mockito.when(memberService.loginToken("admin_token"))
                .thenReturn(output);
        request.setCookies(new Cookie("token", "admin_token"));

        final var result = sut.preHandle(request, response, null);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("토큰의 정보가 관리자가 아니면 거짓을 반환한다.")
    void return_false_when_token_info_is_not_admin() {
        final var output = new TokenLoginOutput(1, "조이썬", "joyson5582@email.com", "password1234", Role.USER.getValue());
        Mockito.when(memberService.loginToken("not_admin_token"))
                .thenReturn(output);
        request.setCookies(new Cookie("token", "not_admin_token"));

        final var result = sut.preHandle(request, response, null);
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("토큰이 없으면 거짓을 반환한다.")
    void return_false_when_not_exist_token() {
        final var result = sut.preHandle(request, response, null);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(result).isFalse();
    }
}
