package roomescape.domain.login;

import org.springframework.stereotype.Service;
import roomescape.domain.member.model.Member;
import roomescape.domain.member.service.MemberService;
import roomescape.global.auth.JwtProvider;
import roomescape.global.auth.JwtRequest;

@Service
public class LoginService {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    public LoginService(JwtProvider jwtProvider, MemberService memberService) {
        this.jwtProvider = jwtProvider;
        this.memberService = memberService;
    }

    public String requestLogin(LoginRequest loginRequest) {
        Member member = memberService.getMemberOf(loginRequest.email(), loginRequest.password());
        return jwtProvider.createToken(JwtRequest.from(member));
    }
}
