package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.UserResponse;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.user.domain.User;
import roomescape.user.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(TokenRequest request) {
        String email = request.email();
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 이메일입니다."));

        validatePassword(findUser, request.password());

        return jwtTokenProvider.createToken(findUser);
    }

    public UserResponse getUserData(String token) {
        jwtTokenProvider.validateToken(token);
        Long userId = Long.parseLong(jwtTokenProvider.getSubject(token));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return UserResponse.from(user);
    }

    private void validatePassword(User user, String requestPassword) {
        if (user.getPassword().equals(requestPassword)) {
            return;
        }

        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

}
