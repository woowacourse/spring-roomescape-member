package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(MemberRequest memberRequest) {
        return jwtTokenProvider.createToken(memberRequest.email());
    }

    public MemberResponse findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    private MemberResponse findMember(String principal) {
        return new MemberResponse(principal);
    }
}
