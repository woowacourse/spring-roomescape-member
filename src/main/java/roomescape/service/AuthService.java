package roomescape.service;

import org.springframework.stereotype.Service;

import roomescape.dto.request.MemberCreateRequest;
import roomescape.dto.response.TokenResponse;
import roomescape.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(MemberCreateRequest tokenRequest) {
        String accessToken = jwtTokenProvider.createToken(tokenRequest.email());
        return new TokenResponse(accessToken);
    }
}
