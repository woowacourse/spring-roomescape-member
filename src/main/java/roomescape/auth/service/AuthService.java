package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.global.auth.jwt.JwtHandler;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;

@Service
public class AuthService {
    private final MemberService memberService;
    private final JwtHandler jwtHandler;

    public AuthService(final MemberService memberService, final JwtHandler jwtHandler) {
        this.memberService = memberService;
        this.jwtHandler = jwtHandler;
    }

    public LoginResponse login(final LoginRequest request) {
        Member member = memberService.findMemberByEmailAndPassword(request.email(), request.password());

        String accessToken = jwtHandler.createToken(member.getId());
        return new LoginResponse(member.getId(), accessToken);
    }

    public LoginCheckResponse checkLogin(final Long memberId) {
        Member member = memberService.findMemberById(memberId);

        return new LoginCheckResponse(member.getName());
    }
}
