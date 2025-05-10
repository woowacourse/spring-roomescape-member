package roomescape.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.auth.dto.MemberAuthRequest;

@Component
public class MemberAuthRequestExtractor {
    private final TokenExtractor tokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberAuthRequestExtractor(TokenExtractor tokenExtractor, JwtTokenProvider jwtTokenProvider) {
        this.tokenExtractor = tokenExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberAuthRequest extract(HttpServletRequest request) {
        String token = tokenExtractor.extract(request);
        String payload = jwtTokenProvider.getPayload(token);
        return new MemberAuthRequest(Long.parseLong(payload));
    }
}
