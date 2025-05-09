package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.entity.User;
import roomescape.auth.repository.UserRepository;
import roomescape.auth.service.dto.response.CheckResponse;
import roomescape.auth.service.dto.request.LoginRequest;
import roomescape.auth.service.dto.response.LoginResponse;
import roomescape.auth.service.dto.request.SignupRequest;
import roomescape.exception.badRequest.BadRequestException;
import roomescape.exception.conflict.UserEmailConflictException;
import roomescape.exception.notFound.UserNotFoundException;
import roomescape.exception.unauthorized.UserUnauthorizedException;

@Service
public class UserAuthService {
    private final UserRepository userRepository;
    // TODO: TokenProvider 인터페이스 삭제 - 테스트 Stub으로 변경
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
                    throw new UserEmailConflictException();
                }, () -> {
                    User user = request.toEntity();
                    userRepository.save(user);
                });
    }

    public CheckResponse checkLogin(String token) {
        String subject = jwtTokenProvider.resolve(token);
        try {
            final long userId = Long.parseLong(subject);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));
            return new CheckResponse(user.getName());
        } catch (NumberFormatException e) {
            throw new BadRequestException("잘못된 형식의 토큰입니다.");
        }
    }
}
