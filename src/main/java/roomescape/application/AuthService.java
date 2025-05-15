package roomescape.application;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import roomescape.application.exception.AuthException;
import roomescape.domain.Member;
import roomescape.domain.Role;
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
        LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getRole(), member.getEmail());
        return jwtTokenProvider.createToken(loginMember);
    }

    public LoginMember findLoginMemberByToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthException("[ERROR] 유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
        }
        Long id = jwtTokenProvider.getId(token);
        String name = jwtTokenProvider.getName(token);
        Role role = jwtTokenProvider.getRole(token);
        String email = jwtTokenProvider.getEmail(token);
        return new LoginMember(id, name, role, email);
    }

    public Role findRoleByToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthException("[ERROR] 유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
        }
        return jwtTokenProvider.getRole(token);
    }
}
