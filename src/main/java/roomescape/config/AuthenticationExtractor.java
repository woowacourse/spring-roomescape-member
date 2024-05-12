package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import roomescape.constant.CommonKey;
import roomescape.domain.member.Member;
import roomescape.exception.AuthorizationException;
import roomescape.service.AuthService;

@Component
public class AuthenticationExtractor {

    private static final String UNAUTHORIZED_MESSAGE = "접근 권한이 없는 요청입니다.";

    private final AuthService authService;

    public AuthenticationExtractor(AuthService authService) {
        this.authService = authService;
    }

    public Member extractAuthInfo(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthorizationException(UNAUTHORIZED_MESSAGE);
        }
        Cookie cookie = Arrays.stream(cookies)
                .filter(element -> CommonKey.TOKEN_KEY.isTokenKey(element.getName()))
                .findFirst()
                .orElseThrow(() -> new AuthorizationException(UNAUTHORIZED_MESSAGE));

        String token = cookie.getValue();

        return authService.findAuthInfo(token);
    }

    public void validateRole(HttpServletRequest request) {
        Member member = extractAuthInfo(request);
        if (member.isNotAdmin()) {
            throw new AuthorizationException(UNAUTHORIZED_MESSAGE);
        }
    }
}
