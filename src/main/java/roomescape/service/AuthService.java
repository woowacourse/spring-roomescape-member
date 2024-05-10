package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.user.User;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.service.dto.login.LoginRequest;

@Service
public class AuthService {

    private JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(LoginRequest loginRequest) {
        // TODO: 회원 조회
        return jwtTokenProvider.createToken(new User(1L, "naknak"));
    }
}
