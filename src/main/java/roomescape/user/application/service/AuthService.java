package roomescape.user.application.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import roomescape.user.InvalidUserException;
import roomescape.user.application.repository.UserRepository;
import roomescape.user.domain.User;
import roomescape.user.infrastructure.JwtTokenProvider;
import roomescape.user.presentation.dto.LoginRequest;
import roomescape.user.presentation.dto.TokenResponse;

@Service
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    public AuthService(JwtTokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    public TokenResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new InvalidUserException("이메일이 올바르지 않습니다.", HttpStatus.NOT_FOUND));
        validateUserLogin(user, loginRequest);
        return tokenProvider.createToken(user);
    }

    public User getUser(String token) {
        String email = tokenProvider.resolveToken(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidUserException("존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND));
    }

    private void validateUserLogin(User user, LoginRequest loginRequest) {
        if (!user.getEmail().equals(loginRequest.email()) ||
                !user.getPassword().equals(loginRequest.password())) {
            throw new InvalidUserException("비밀번호가 틀렸습니다.", HttpStatus.UNAUTHORIZED);
        }
    }
}
