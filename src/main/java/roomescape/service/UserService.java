package roomescape.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.auth.JwtProvider;
import roomescape.domain.User;
import roomescape.service.dto.request.LoginServiceRequest;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtProvider jwtProvider;

    public String login(LoginServiceRequest request) {
        return jwtProvider.createToken(new User(1L, "name", request.email(), request.password()));
    }
}
