package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.exception.InvalidInputException;
import roomescape.util.JwtTokenProvider;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Cookie generateCookie(Member member) {
        Cookie cookie = new Cookie("token", jwtTokenProvider.createToken(member));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Long findMemberId(Cookie[] cookies) {
        if (cookies == null) {

        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                String token = cookie.getValue();
                return getMemberIdFromCookie(token);
            }
        }
        throw new InvalidInputException("잘못된 요청입니다.");
    }

    private Long getMemberIdFromCookie(String token) {
        boolean isValid = jwtTokenProvider.validateToken(token);
        if (isValid) {
            return jwtTokenProvider.getMemberIdFromToken(token);
        }
        throw new InvalidInputException("시간이 지나 자동 로그아웃 되었습니다. 다시 로그인 해주세요.");
    }
}
