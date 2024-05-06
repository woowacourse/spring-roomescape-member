package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.TokenResponse;

@Service
public class AuthService {

    public TokenResponse createToken(LoginRequest loginRequest) {
        return new TokenResponse(
                "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJudWxsIiwibmFtZSI6ImFkbWluQGVtYWlsLmNvbSIsImVtYWlsIjoiYWRtaW5AZW1haWwuY29tIn0.2Ah_xQricNuO_j7hKitSUPsAAbbb-WlwQJumO_uAw2qzVC_c-peCM1xwFC78Y7ALAE0oIPwRPf0C9G_s0ZQtOg");
    }
}
