package roomescape.application;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.repository.MemberRepository;
import roomescape.dto.auth.TokenRequest;
import roomescape.dto.auth.TokenResponse;
import roomescape.global.exception.BadRequestException;
import roomescape.global.exception.ForbiddenException;
import roomescape.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        if (!memberRepository.existsByEmailAndPassword(tokenRequest.email(), tokenRequest.password())) {
            throw new BadRequestException("로그인 정보가 잘못되었습니다.");
        }
        Member member = memberRepository.findByEmail(tokenRequest.email())
                .orElseThrow(() -> new BadRequestException("해당 이메일이 존재하지 않습니다."));

        String accessToken = jwtTokenProvider.createToken(member);
        return new TokenResponse(accessToken);
    }

    public Member findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token, "name");

        return memberRepository.findByName(payload)
                .orElseThrow(() -> new ForbiddenException("토큰에 해당하는 정보가 없습니다."));
    }
}
