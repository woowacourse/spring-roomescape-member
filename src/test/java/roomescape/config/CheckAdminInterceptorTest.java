package roomescape.config;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.LoginRequest;
import roomescape.dto.TokenResponse;
import roomescape.service.AuthService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/testdata.sql")
class CheckAdminInterceptorTest {

    @Autowired
    CheckAdminInterceptor checkAdminInterceptor;
    @Autowired
    AuthService authService;

    @Test
    @DisplayName("로그인한 사용자가 관리자인지 알 수 있다.")
    void preHandle() {
        final TokenResponse tokenResponse = authService.createToken(new LoginRequest("789@email.com", "789"));
        final String token = tokenResponse.accessToken();
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setCookies(new Cookie("token", token));

        final boolean result = checkAdminInterceptor.preHandle(mockHttpServletRequest, null, null);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("로그인한 사용자가 일반 유저라면 예외가 발생한다.")
    void invalidPreHandle() {
        final TokenResponse tokenResponse = authService.createToken(new LoginRequest("123@email.com", "123"));
        final String token = tokenResponse.accessToken();
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setCookies(new Cookie("token", token));

        assertThatThrownBy(() -> checkAdminInterceptor.preHandle(mockHttpServletRequest, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("관리자 페이지는 관리자만 들어올 수 있습니다.");
    }
}
