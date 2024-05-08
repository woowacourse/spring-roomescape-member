package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.LoginMember;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberPayload;
import roomescape.dto.TokenDto;
import roomescape.exception.ClientErrorExceptionWithData;
import roomescape.infrastructure.JwtProvider;
import roomescape.infrastructure.PasswordEncoder;

@Service
public class AuthService {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(MemberService memberService, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public TokenDto login(LoginRequest request) {
        Member loginMember = authenticateUser(request);
        MemberPayload loginMemberPayload = MemberPayload.from(loginMember);
        String token = jwtProvider.createToken(loginMemberPayload);
        return new TokenDto(token);
    }

    public LoginMember findUserByToken(TokenDto tokenDto) {
        Long userId = extractUserIdByToken(tokenDto.accessToken());
        Member member = memberService.getMemberById(userId);
        return LoginMember.from(member);
    }

    private Member authenticateUser(LoginRequest request) {
        Member member = memberService.getMemberByEmail(request.email());
        validatePassword(request, member);
        return member;
    }

    private void validatePassword(LoginRequest request, Member memberToLogin) {
        if (!passwordEncoder.matches(request.password(), memberToLogin.getEncodedPassword())) {
            throw new ClientErrorExceptionWithData("[ERROR] 잘못된 비밀번호 입니다.");
        }
    }

    private Long extractUserIdByToken(String accessToken) {
        String userId = jwtProvider.getSubject(accessToken);
        try {
            return Long.valueOf(userId);
        } catch (NumberFormatException e) {
            throw new ClientErrorExceptionWithData("[ERROR] 유효한 토큰이 아닙니다.", userId);
        }
    }
}
