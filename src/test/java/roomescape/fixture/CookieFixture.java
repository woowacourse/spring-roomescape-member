package roomescape.fixture;

import org.springframework.boot.test.context.TestComponent;

import io.restassured.http.Cookie;
import roomescape.util.TokenProvider;

@TestComponent
public class CookieFixture {

    private final TokenProvider tokenProvider;

    public CookieFixture(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public Cookie adminCookie() {
        return cookie("admin@woowa.com");
    }

    public Cookie userCookie() {
        return cookie("zeus@woowa.com");
    }

    public Cookie cookie(String value) {
        String token = tokenProvider.create(value);
        return new Cookie.Builder("token", token).build();
    }
}
