package roomescape.business.service;

import org.springframework.stereotype.Service;
import roomescape.auth.jwt.JwtUtil;
import roomescape.business.model.entity.User;
import roomescape.business.model.repository.UserRepository;
import roomescape.business.model.vo.AuthToken;
import roomescape.exception.auth.LoginFailException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(final UserRepository userRepository, final JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public AuthToken authenticate(final String email, final String password) {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(LoginFailException::new);

        if (!user.isPasswordCorrect(password)) {
            throw new LoginFailException();
        }

        return jwtUtil.createToken(user);
    }
}
