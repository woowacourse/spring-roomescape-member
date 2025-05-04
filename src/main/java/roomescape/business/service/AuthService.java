package roomescape.business.service;

import org.springframework.stereotype.Service;
import roomescape.business.model.entity.User;
import roomescape.business.model.repository.UserRepository;
import roomescape.business.model.vo.Authentication;
import roomescape.exception.LoginFailException;
import roomescape.jwt.JwtUtil;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(final UserRepository userRepository, final JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public Authentication authenticate(final String email, final String password) {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(LoginFailException::new);

        if (!user.isPasswordCorrect(password)) {
            throw new LoginFailException();
        }

        // TODO : secret을 진짜로 비밀로 작성
        return jwtUtil.getAuthentication(user);
    }
}
