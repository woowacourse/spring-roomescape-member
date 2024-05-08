package roomescape.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;

@Component
public class CookieGenerator {
    private static final boolean HTTP_ONLY_OPTION = true;
    private static final String ALLOWED_PATH = "/";

    private final JwtTokenProvider tokenProvider;

    public CookieGenerator(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public Cookie generate(Member member) {
        Cookie cookie = new Cookie("token", tokenProvider.generateToken(member));
        cookie.setHttpOnly(HTTP_ONLY_OPTION);
        cookie.setPath(ALLOWED_PATH);
        return cookie;
    }
}
