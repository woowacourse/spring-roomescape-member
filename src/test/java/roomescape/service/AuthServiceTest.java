package roomescape.service;

import jakarta.servlet.http.Cookie;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.model.User;
import roomescape.service.fake.FakeJwtTokenProvider;

class AuthServiceTest {

    private AuthService authService = new AuthService(new FakeJwtTokenProvider());

    @DisplayName("사용자를 제공하면 쿠키를 만들어 준다.")
    @Test
    void should_create_cookie_when_given_user() {
        User user = new User(1L, "썬", "sun@email.com", "1234");

        Cookie cookie = authService.createCookieByUser(user);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(cookie.getName()).isEqualTo("token");
            softAssertions.assertThat(cookie.getPath()).isEqualTo("/");
            softAssertions.assertThat(cookie.isHttpOnly()).isTrue();
        });
    }
}
