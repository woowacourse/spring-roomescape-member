package roomescape.service;

import static roomescape.exception.ExceptionType.NOT_FOUND_USER;
import static roomescape.exception.ExceptionType.WRONG_PASSWORD;

import java.util.Map;
import org.springframework.stereotype.Service;
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
        User findUser = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new RoomescapeException(NOT_FOUND_USER));
        if (!findUser.getPassword().equals(loginRequest.password())) {
            throw new RoomescapeException(WRONG_PASSWORD);
        }

        return jwtGenerator.generateWith(Map.of(
                "name", findUser.getName(),
                "email", findUser.getEmail()
        ));
    }
}
