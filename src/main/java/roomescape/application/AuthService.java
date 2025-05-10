package roomescape.application;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.infrastructure.jwt.JwtTokenProvider;
import roomescape.presentation.dto.request.LoginRequest;
import roomescape.presentation.dto.LoginMember;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberService.findMemberByEmail(loginRequest.email());
        if (member.isIncorrectPassword(loginRequest.password())) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(loginRequest.email());
    }

    public LoginMember findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return LoginMember.from(memberService.findMemberByEmail(payload));
    }
}
