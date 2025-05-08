package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.global.security.JwtTokenProvider;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;

@Service
public class AuthService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LoginRequest request) {
        Member member = memberService.findByEmailAndPassword(request.email(), request.password());
        return jwtTokenProvider.createToken(member);
    }

    public LoginCheckResponse loginCheck(String token) {
        Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(token));
        Member member = memberService.findById(memberId);
        return LoginCheckResponse.from(member);
    }
}
