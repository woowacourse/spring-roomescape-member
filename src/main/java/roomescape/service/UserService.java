package roomescape.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.domain.User;
import roomescape.domain.repository.UserRepository;
import roomescape.exception.user.AuthenticationFailureException;
import roomescape.service.security.JwtUtils;
import roomescape.web.dto.request.LoginRequest;
import roomescape.web.dto.response.UserResponse;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String login(LoginRequest loginRequest) {
        User findUser = userRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(AuthenticationFailureException::new);

        return "token=" + JwtUtils.encode(findUser);
    }

    public UserResponse findUserByToken(String token) {
        Long decodedId = JwtUtils.decode(token);
        User findUser = userRepository.findById(decodedId)
                .orElseThrow(AuthenticationFailureException::new);
        return new UserResponse(findUser.getName());
    }
}
