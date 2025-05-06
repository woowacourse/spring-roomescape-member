package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createTokenByMember(Member member) {
        return jwtTokenProvider.createTokenByMember(member);
    }
}
