package roomescape.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.error.NotFoundException;
import roomescape.error.UnauthorizedException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String CLAIM_NAME = "name";
    private static final String CLAIM_ROLE = "role";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public boolean checkInvalidLogin(final String principal, final String credentials) {
        final Member member = memberRepository.findByEmail(principal)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 email입니다."));
        return !member.getEmail().equals(principal) || !member.getPassword().equals(credentials);
    }

    public String createToken(final LoginRequest loginRequest) {
        if (checkInvalidLogin(loginRequest.email(), loginRequest.password())) {
            throw new UnauthorizedException("이메일 또는 패스워드가 올바르지 않습니다.");
        }
        final Member member = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 멤버입니다."));
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
        final Long memberId = Long.valueOf(jwtTokenProvider.getSubject(token));
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다."));
        return new LoginCheckResponse(member);
    }
}
