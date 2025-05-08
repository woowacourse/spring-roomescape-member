package roomescape.service;

import org.springframework.stereotype.Service;

import roomescape.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import roomescape.auth.JwtProvider;
import roomescape.domain.User;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtProvider jwtProvider;

    public String login(LoginRequest request) {
        return jwtProvider.createToken(new User(1L, "name", request.email(), request.password()));
    }
}
