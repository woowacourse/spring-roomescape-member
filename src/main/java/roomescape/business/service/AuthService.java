package roomescape.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.AuthToken;
import roomescape.auth.jwt.JwtUtil;
import roomescape.business.model.entity.User;
import roomescape.business.model.repository.UserRepository;
import roomescape.exception.auth.LoginFailException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthToken authenticate(final String email, final String password) {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new LoginFailException("이메일이 잘못되었습니다."));

        if (!user.isPasswordCorrect(password)) {
            throw new LoginFailException("비밀번호가 잘못되었습니다.");
        }

        return jwtUtil.createToken(user);
    }
}
