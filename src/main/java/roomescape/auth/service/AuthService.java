package roomescape.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.LoginResponse;
import roomescape.auth.exception.AuthException;
import roomescape.auth.exception.UnAuthorizedException;
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

    public LoginMember makeLoginMember(final String token, final MemberRole memberRole) {
        validateToken(token);
        Claims claims = jwtProvider.getAllClaimsFromToken(token);
        Long memberId = Long.valueOf(claims.getSubject());
        validateMember(memberId, memberRole);
        return new LoginMember(memberId, (String) claims.get("name"),
                MemberRole.from((String) claims.get("role")));
    }

    private void validateToken(final String token) {
        if (jwtProvider.isInvalidToken(token)) {
            throw new AuthException("유효하지 않은 토큰입니다.");
        }
    }

    private Member getMemberByEmailAndPassword(final String email, final String password) {
        return memberRepository.findMemberByEmailAndPassword(email, password)
                .orElseThrow(() -> new AuthException("존재하지 않은 사용자입니다."));
    }

    private void validateMember(final Long id, final MemberRole memberRole) {
        if (memberRole == MemberRole.USER) {
            validateExistsById(id);
            return;
        }
        validateExistsByIdAndMemberRole(id, memberRole);
    }

    private void validateExistsById(final Long id) {
        if (!memberRepository.existsById(id)) {
            throw new AuthException("존재하지 않은 사용자입니다.");
        }
    }

    private void validateExistsByIdAndMemberRole(final Long id, final MemberRole memberRole) {
        if (!memberRepository.existsByIdAndMemberRole(id, memberRole)) {
            throw new UnAuthorizedException("접근 권한을 벗어났습니다.");
        }
    }
}
