package roomescape.auth.service;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.infrastructure.jwt.JwtHandler;
import roomescape.auth.web.controller.request.LoginRequest;
import roomescape.auth.web.controller.response.MemberNameResponse;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtHandler jwtHandler;
    private final MemberService memberService;

    public String login(LoginRequest request) {
        Member member = memberService.getMember(request.email(), request.password());

        return jwtHandler.createToken(member);
    }

    public MemberNameResponse check(Long memberId) {
        Member member = memberService.getMember(memberId);

        return new MemberNameResponse(member.getName());
    }

    public boolean isAdmin(String token) {
        return jwtHandler.isAdmin(token);
    }

    public Object getMemberId(String token) {
        return jwtHandler.getMemberId(token);
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
