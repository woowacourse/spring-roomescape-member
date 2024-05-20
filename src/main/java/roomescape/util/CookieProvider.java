package roomescape.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import roomescape.controller.api.dto.request.MemberAuthRequest;
import roomescape.controller.api.dto.response.MemberResponse;
import roomescape.exception.CustomBadRequest;

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

    public MemberAuthRequest extractToken(final HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("accessToken"))
                .findAny()
                .map(cookie -> tokenProvider.parse(cookie.getValue()))
                .orElseThrow(() -> new CustomBadRequest("토큰이 없습니다."));
    }
}
