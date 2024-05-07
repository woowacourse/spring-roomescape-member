package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.request.LoginRequest;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;



    public String createToken(LoginRequest loginRequest) {
        return null;
    }
}
