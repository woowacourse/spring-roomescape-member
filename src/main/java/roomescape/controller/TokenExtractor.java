package roomescape.controller;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;

public class TokenExtractor {

    private TokenExtractor() {
    }
    

    public static String extractTokenFromCookie(Cookie[] cookies) {

        Optional<String> token = Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals("token"))
            .map(Cookie::getValue)
            .findFirst();

        return token.orElseThrow(() -> new IllegalArgumentException("토큰이 존재하지 않습니다."));
    }
}
