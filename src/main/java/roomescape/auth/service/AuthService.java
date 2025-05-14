package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.domain.JwtTokenProvider;
import roomescape.auth.dto.request.TokenRequest;
import roomescape.auth.exception.AuthorizationException;
import roomescape.member.domain.Member;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.service.MemberService;

@Service
public class AuthService {
    private final JwtTokenProvider tokenProvider;
    private final MemberService memberService;

    public AuthService(final JwtTokenProvider tokenProvider, final MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.memberService = memberService;
    }

    public String createToken(final TokenRequest request) {
        try {
            Member member = memberService.getMember(request.email(), request.password());
            return tokenProvider.createToken(member.getId().toString());
        } catch (MemberNotFoundException e) {
            throw new AuthorizationException();
        }
    }

    public Member findMemberByToken(final String token) {
        Long id = Long.valueOf(tokenProvider.getPayload(token));
        return memberService.getMember(id);
    }
}
