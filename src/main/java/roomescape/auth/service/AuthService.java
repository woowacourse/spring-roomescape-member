package roomescape.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.auth.exception.AuthException;
import roomescape.auth.infrastructure.JwtProvider;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;
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
        Member member = getMemberByEmailAndPassword(loginRequest.email(), loginRequest.password());
        String accessToken = jwtProvider.createToken(member);
        return new LoginResponse(accessToken);
    }

    public String extractTokenFromCookie(final String cookieHeader) {
        String[] cookies = cookieHeader.split(";");
        for (String cookie : cookies) {
            if (cookie.startsWith("token=")) {
                return cookie.substring(6);
            }
        }
        return null;
    }

    public LoginMember makeLoginMember(final String token) {
        validateToken(token);
        Claims claims = jwtProvider.getAllClaimsFromToken(token);
        return new LoginMember(Long.valueOf(claims.getSubject()), (String) claims.get("name"),
                MemberRole.from((String) claims.get("role")));
    }

    private Member getMemberByEmailAndPassword(final String email, final String password) {
        return memberRepository.findByMember(email, password)
                .orElseThrow(() -> new AuthException("존재하지 않은 email 또는 비밀번호입니다."));
    }

    private void validateToken(final String token) {
        if (jwtProvider.isInvalidToken(token)) {
            throw new AuthException("유효하지 않은 토큰입니다.");
        }
    }

}
