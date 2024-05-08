package roomescape.service;

import static roomescape.exception.ExceptionType.NOT_FOUND_USER;
import static roomescape.exception.ExceptionType.WRONG_PASSWORD;

import io.jsonwebtoken.Claims;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.LoginUser;
import roomescape.domain.User;
import roomescape.dto.LoginRequest;
import roomescape.exception.RoomescapeException;
import roomescape.repository.UserRepository;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final JwtGenerator jwtGenerator;

    public LoginService(UserRepository userRepository, JwtGenerator jwtGenerator) {
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
    }

    public String getLoginToken(LoginRequest loginRequest) {
        Optional<User> byEmail = userRepository.findByEmail(loginRequest.email());
        User findUser = byEmail
                .orElseThrow(() -> new RoomescapeException(NOT_FOUND_USER));
        if (!findUser.getPassword().equals(loginRequest.password())) {
            throw new RoomescapeException(WRONG_PASSWORD);
        }

        return jwtGenerator.generateWith(Map.of(
                "id", findUser.getId(),
                "name", findUser.getName(),
                "email", findUser.getEmail()
        ));
    }

    public LoginUser checkLogin(String token) {
        Claims claims = jwtGenerator.getClaims(token);
        return new LoginUser(
                claims.get("id", Long.class),
                claims.get("name", String.class),
                claims.get("email", String.class)
        );
    }
}
