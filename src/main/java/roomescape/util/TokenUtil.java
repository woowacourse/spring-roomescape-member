package roomescape.util;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import jakarta.servlet.http.Cookie;

import org.springframework.stereotype.Component;

import roomescape.exception.AuthenticationException;

@Component
public class TokenUtil implements CookieUtil {

    private static final String NAME = "token";

    private final TokenProvider jwtProvider;

    public TokenUtil(TokenProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Cookie create(String value) {
        String token = jwtProvider.create(value);
        Cookie cookie = new Cookie(NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    @Override
    public Cookie expired() {
        Cookie cookie = new Cookie(NAME, null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    @Override
    public Optional<String> extractValue(Cookie[] cookies) {
        return Stream.of(CookieUtil.requireNonnull(cookies, AuthenticationException::new))
                .filter(cookie -> Objects.equals(NAME, cookie.getName()))
                .map(Cookie::getValue)
                .map(jwtProvider::extract)
                .findFirst();
    }
}
