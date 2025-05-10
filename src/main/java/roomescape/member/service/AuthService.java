package roomescape.member.service;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.controller.dto.LoginCheckResponse;
import roomescape.member.controller.dto.LoginRequest;
import roomescape.member.auth.dto.MemberInfoResponse;
import roomescape.member.domain.Member;
import roomescape.member.auth.JwtTokenExtractor;
import roomescape.member.auth.JwtTokenProvider;
import roomescape.member.domain.MemberName;

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
        MemberName name = jwtTokenExtractor.extractMemberNameFromCookie(cookies);
        return new LoginCheckResponse(name.getValue());
    }

    public MemberInfoResponse getMemberInfo(Cookie[] cookies) {
        Member member = memberService.findMemberById(jwtTokenExtractor.extractMemberIdFromCookie(cookies));
        return new MemberInfoResponse(
                member.getId().getValue(),
                member.getName().getValue(),
                member.getEmail().getValue());
    }
}
