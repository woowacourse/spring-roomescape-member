package roomescape.config;

import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.dto.TokenResponse;
import roomescape.service.AuthService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LoginMemberArgumentResolverTest {

    @Autowired
    LoginMemberArgumentResolver loginMemberArgumentResolver;
    @Autowired
    AuthService authService;

    @Test
    @DisplayName("쿠키를 통해 해당 유저를 알 수 있다.")
    @Sql("/testdata.sql")
    void resolveArgument() {
        final TokenResponse tokenResponse = authService.createToken(new LoginRequest("123@email.com", "123"));
        final String token = tokenResponse.accessToken();
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setCookies(new Cookie("token", token));
        NativeWebRequest request = new ServletWebRequest(mockHttpServletRequest);

        final Member member = loginMemberArgumentResolver.resolveArgument(null, null, request, null);

        Assertions.assertThat(member.getName()).isEqualTo("뽀로로");
    }
}
