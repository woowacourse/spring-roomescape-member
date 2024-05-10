package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.login.LoginMember;
import roomescape.dto.login.LoginRequest;
import roomescape.dto.member.MemberPayload;
import roomescape.dto.token.TokenDto;
import roomescape.exception.ClientErrorExceptionWithLog;
import roomescape.infrastructure.JwtProvider;
import roomescape.infrastructure.PasswordEncoder;
import roomescape.repository.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public TokenDto login(LoginRequest request) {
        Member loginMember = authenticateUser(request);
        MemberPayload loginMemberPayload = MemberPayload.from(loginMember);
        String token = jwtProvider.createToken(loginMemberPayload);
        return new TokenDto(token);
    }

    public LoginMember checkLogin(TokenDto tokenDto) {
        String token = tokenDto.accessToken();
        Long userId = extractUserIdByToken(token);
        return new LoginMember(userId);
    }

    private Member authenticateUser(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email()).orElseThrow();
        validatePassword(request, member);
        return member;
    }

    private Long extractUserIdByToken(String token) {
        validateToken(token);
        String tokenSubject = jwtProvider.getSubject(token);
        return parseLong(tokenSubject);
    }

    private void validatePassword(LoginRequest request, Member memberToLogin) {
        if (!passwordEncoder.matches(request.password(), memberToLogin.getEncodedPassword())) {
            throw new ClientErrorExceptionWithLog("[ERROR] 잘못된 비밀번호 입니다.");
        }
    }

    private void validateToken(String token) {
        if (jwtProvider.isValidateToken(token)) {
            throw new ClientErrorExceptionWithLog("[ERROR] 유효한 토큰이 아닙니다.");
        }
    }

    private Long parseLong(String value) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            throw new ClientErrorExceptionWithLog(
                    "[ERROR] 토큰의 사용자 ID 형식이 유효하지 않습니다.",
                    "subject(userId) : " + value
            );
        }
    }
}
