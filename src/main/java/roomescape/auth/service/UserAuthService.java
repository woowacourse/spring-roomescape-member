package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.entity.User;
import roomescape.auth.repository.UserRepository;
import roomescape.auth.service.dto.LoginRequest;
import roomescape.auth.service.dto.LoginResponse;
import roomescape.auth.service.dto.SignupRequest;
import roomescape.exception.conflict.ConflictException;
import roomescape.exception.unauthorized.UserUnauthorizedException;

@Service
public class UserAuthService {
    private final UserRepository userRepository;
    private final TokenProvider jwtTokenProvider;

    public UserAuthService(UserRepository userRepository, TokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(UserUnauthorizedException::new);
        String token = jwtTokenProvider.createToken(user);
        return new LoginResponse(token);
    }

    public void signup(SignupRequest request) {
        userRepository.findByEmail(request.email())
                .ifPresentOrElse(user -> {
                    throw new ConflictException("이미 존재하는 유저입니다.");
                }, () -> {
                    User user = request.toEntity();
                    userRepository.save(user);
                });
    }
}
