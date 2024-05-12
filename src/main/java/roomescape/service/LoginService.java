package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.request.MemberCreateRequest;
import roomescape.dto.response.TokenResponse;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.MemberRepository;

@Service
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public LoginService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenResponse createToken(MemberCreateRequest memberCreateRequest) {
        Member member = memberRepository.findByEmail(memberCreateRequest.email());
        return new TokenResponse(jwtTokenProvider.createToken(member));
    }

    public Member createMember(Cookie[] cookies) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(cookies);
        return memberRepository.findById(memberId);
    }
}
