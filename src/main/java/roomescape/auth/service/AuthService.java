package roomescape.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import roomescape.auth.dto.CheckLoginResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.auth.exception.AuthException;
import roomescape.auth.infrastructure.JwtProvider;
import roomescape.member.domain.Member;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.repository.MemberRepository;

@Component
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public AuthService(final JwtProvider jwtProvider, final MemberRepository memberRepository) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }

    public LoginResponse createToken(final LoginRequest loginRequest) {
        Member member = getByMember(loginRequest);

        String accessToken = jwtProvider.createToken(member);
        return new LoginResponse(accessToken);
    }

    public CheckLoginResponse findNameByToken(final String token) {
        validateToken(token);
        Claims claims = jwtProvider.getAllClaimsFromToken(token);
        validateMemberExists(claims);
        return new CheckLoginResponse((String) claims.get("name"));
    }

    private Member getByMember(final LoginRequest loginRequest) {
        return memberRepository.findByMember(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new AuthException("존재하지 않은 email 또는 비밀번호입니다."));
    }

    private void validateToken(final String token) {
        if (jwtProvider.isInvalidToken(token)) {
            throw new AuthException("유효하지 않은 토큰입니다.");
        }
    }

    private void validateMemberExists(final Claims claims) {
        if (!memberRepository.existsById(Long.valueOf(claims.getSubject()))) {
            throw new MemberNotFoundException("존재하지 않은 사용자입니다.");
        }
    }
}
