package roomescape.member.service;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.controller.dto.LoginCheckResponse;
import roomescape.member.controller.dto.LoginRequest;
import roomescape.member.domain.Member;
import roomescape.member.service.auth.JwtTokenExtractor;
import roomescape.member.service.auth.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenExtractor jwtTokenExtractor;

    public String login(LoginRequest loginRequest) {
        Member member = memberService.findMember(loginRequest);
        return jwtTokenProvider.generateToken(member);
    }

    public LoginCheckResponse checkLogin(Cookie[] cookies) {
        return new LoginCheckResponse(jwtTokenExtractor.extractMemberNameFromCookie(cookies));
    }
}
