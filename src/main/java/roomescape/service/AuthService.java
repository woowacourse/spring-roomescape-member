package roomescape.service;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.exception.InvalidInputException;
import roomescape.util.JwtTokenProvider;

@Service
public class AuthService {
    private static final String TOKEN = "token";
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Cookie generateCookie(Member member) {
        Cookie cookie = new Cookie(TOKEN, jwtTokenProvider.createToken(member));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Long findMemberId(Cookie[] cookies) {
        String token = getTokenFromCookies(cookies);
        return jwtTokenProvider.getMemberIdFromToken(token);
    }

    public boolean isAdmin(Cookie[] cookies) {
        String token = getTokenFromCookies(cookies);
        return Role.ADMIN == jwtTokenProvider.getMemberRoleFromToken(token);
    }

    private String getTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            throw new InvalidInputException("로그인 해주세요.");
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN))
                .findAny()
                .map(this::getValidatedToken)
                .orElseThrow(() -> new InvalidInputException("잘못된 요청입니다."));
    }

    private String getValidatedToken(Cookie cookie) {
        String token = cookie.getValue();
        if (jwtTokenProvider.validateToken(token)) {
            return token;
        }
        throw new InvalidInputException("다시 로그인 해주세요.");
    }
}
