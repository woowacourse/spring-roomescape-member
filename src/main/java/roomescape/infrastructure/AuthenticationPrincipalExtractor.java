package roomescape.infrastructure.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.application.auth.dto.MemberAuthRequest;

@Component
public class MemberAuthRequestExtractor {
    private final TokenExtractor tokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberAuthRequestExtractor(TokenExtractor tokenExtractor, JwtTokenProvider jwtTokenProvider) {
        this.tokenExtractor = tokenExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberIdDto extract(HttpServletRequest request) {
        String token = jwtTokenExtractor.extract(request);
        String payload = jwtTokenProvider.getPayload(token);
        return new MemberIdDto(Long.parseLong(payload));
    }
}
