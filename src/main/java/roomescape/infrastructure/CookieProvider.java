package roomescape.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import roomescape.controller.api.dto.request.AuthMemberRequest;
import roomescape.controller.api.dto.response.MemberResponse;

@Component
public class CookieProvider {

    private final TokenProvider tokenProvider;

    public CookieProvider(final TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public String create(final MemberResponse response) {
        final var token = tokenProvider.create(response);
        final var cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .path("/")
                .build();
        return String.valueOf(cookie);
    }

    public AuthMemberRequest extractToken(final HttpServletRequest request) {
        final var cookies = request.getCookies();
        for (final var cookie : cookies) {
            if (cookie.getName().equals("accessToken")) {
                return tokenProvider.parse(cookie.getValue());
            }
        }
        return null;
    }
}
