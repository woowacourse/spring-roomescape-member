package roomescape.member.application;

import org.springframework.stereotype.Service;
import roomescape.member.presentation.dto.TokenRequest;

@Service
public class MemberService {
    private final TokenProvider tokenProvider;

    public MemberService(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public String createToken(TokenRequest tokenRequest) {
        return tokenProvider.createToken(tokenRequest.getEmail());
    }
}
