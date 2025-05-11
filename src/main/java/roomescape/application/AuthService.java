package roomescape.application;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.infrastructure.jwt.JwtTokenProvider;
import roomescape.presentation.dto.request.LoginMember;
import roomescape.presentation.dto.request.LoginRequest;

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
        Member member = memberService.findMemberByEmail(payload);
        return new LoginMember(member.getId(), member.getName(), member.getRole(), member.getEmail());
    }
}
