package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.response.MemberProfileResponse;
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

    public MemberProfileResponse findMemberProfile(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                String name = jwtTokenProvider.getMemberNameFromToken(cookie.getValue());
                return new MemberProfileResponse(name);
            }
        }
        throw new RuntimeException();
    }

}
