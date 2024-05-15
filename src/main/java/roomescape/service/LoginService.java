package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.infrastructure.auth.Token;
import roomescape.infrastructure.auth.TokenManager;

@Service
public class LoginService {
    private final TokenManager tokenManager;
    private final MemberService memberService;

    public LoginService(TokenManager tokenManager, MemberService memberService) {
        this.tokenManager = tokenManager;
        this.memberService = memberService;
    }

    public Token login(LoginRequest loginRequest) {
        Member member = memberService.findMemberByEmailAndPassword(loginRequest.email(), loginRequest.password());
        validateLogin(loginRequest, member);
        return tokenManager.generate(member);
    }

    public Member check(Token token) {
        Long memberId = tokenManager.getMemberId(token);
        return memberService.findMemberById(memberId);
    }

    private void validateLogin(LoginRequest loginRequest, Member member) {
        if (loginRequest.password().equals(member.getPassword())) {
            return;
        }
        throw new IllegalArgumentException("[ERROR] 아이디 혹은 비밀번호가 일치 하지 않습니다.");
    }


}
