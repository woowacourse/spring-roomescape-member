package roomescape.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import roomescape.domain.member.Member;
import roomescape.exception.AuthorizationException;
import roomescape.fixture.MemberFixtures;
import roomescape.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthenticationExtractorTest {

    @Mock
    private AuthService authService;

    @Test
    @DisplayName("쿠키 정보를 이용하여 회원 정보를 반환한다.")
    void preHandle() {
        //given
        AuthenticationExtractor authenticationExtractor = new AuthenticationExtractor(authService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        String token = "1234";
        request.setCookies(new Cookie("token", token));
        String name = "daon";
        String email = "test@test.com";
        Member member = MemberFixtures.createAdminMember(name, email);
        given(authService.findAuthInfo(anyString())).willReturn(member);

        //when
        Member result = authenticationExtractor.extractAuthInfo(request);

        //then
        assertAll(
                () -> assertThat(result.getName().getValue()).isEqualTo(name),
                () -> assertThat(result.getEmail().getValue()).isEqualTo(email)
        );
    }

    @Test
    @DisplayName("쿠키가 없는 경우 예외가 발생한다.")
    void preHandleWhenCookiesNull() {
        //given
        AuthenticationExtractor authenticationExtractor = new AuthenticationExtractor(authService);
        MockHttpServletRequest request = new MockHttpServletRequest();

        //when //then
        assertThatThrownBy(() -> authenticationExtractor.extractAuthInfo(request))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("token에 해당하는 쿠키가 없는 경우 예외가 발생한다.")
    void preHandleWhenNotExistTokenCookie() {
        //given
        AuthenticationExtractor authenticationExtractor = new AuthenticationExtractor(authService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("anyString", "1234"));
        MockHttpServletResponse response = new MockHttpServletResponse();

        //when //then
        assertThatThrownBy(() -> authenticationExtractor.extractAuthInfo(request))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName(" 회원 역할이 관리자가 아닐 경우 예외가 발생한다.")
    void preHandleWhenStartsWithAdminAndNotAdmin() {
        //given
        AuthenticationExtractor authenticationExtractor = new AuthenticationExtractor(authService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        String token = "1234";
        String payload = "test@test.com";
        request.setCookies(new Cookie("token", token));
        given(authService.findAuthInfo(token)).willReturn(MemberFixtures.createUserMember("daon", payload));

        //when //then
        assertThatThrownBy(() -> authenticationExtractor.validateRole(request))
                .isInstanceOf(AuthorizationException.class);
    }
}
