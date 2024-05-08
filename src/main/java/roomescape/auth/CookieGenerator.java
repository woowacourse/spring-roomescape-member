package roomescape.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

@Component
public class CookieGenerator {
    private static final boolean HTTP_ONLY_OPTION = true;
    private static final String ALLOWED_PATH = "/";
    private static final String TOKEN_NAME = "token";

    public Cookie generate(Token token) {
        Cookie cookie = new Cookie(TOKEN_NAME, token.getToken());
        cookie.setHttpOnly(HTTP_ONLY_OPTION);
        cookie.setPath(ALLOWED_PATH);
        return cookie;
    }

    public Token getToken(Cookie[] cookies) {
        return new Token(
                Stream.of(cookies)
                        .filter(cookie -> cookie.getName().equals(TOKEN_NAME))
                        .findAny()
                        .orElseThrow(() -> new NoSuchElementException("[ERROR] 토큰을 찾을 수 없습니다."))
                        .getValue()
        );
    }
}
