package roomescape.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.infrastructure.TokenProvider;
import roomescape.error.NotFoundException;
import roomescape.error.UnauthorizedException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String CLAIM_NAME = "name";
    private static final String CLAIM_ROLE = "role";

    private final TokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public String createToken(final LoginRequest loginRequest) {
        final Member member = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UnauthorizedException("존재하지 않는 이메일입니다."));
        if (!member.matchesPassword(loginRequest.password())) {
            throw new UnauthorizedException("이메일 또는 패스워드가 올바르지 않습니다.");
        }
        return jwtTokenProvider.createToken(createClaims(member));
    }

    private Claims createClaims(final Member member) {
        return Jwts.claims()
                .subject(member.getId().toString())
                .add(CLAIM_NAME, member.getName())
                .add(CLAIM_ROLE, member.getRole().name())
                .build();
    }

    public LoginCheckResponse checkLogin(final String token) {
        final Long memberId = Long.valueOf(jwtTokenProvider.extractPrincipal(token));
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다."));
        return new LoginCheckResponse(member);
    }
}
