package roomescape.domain.auth.service;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.auth.config.JwtProperties;
import roomescape.domain.auth.dto.LoginRequest;
import roomescape.domain.auth.dto.LoginUserDto;
import roomescape.domain.auth.dto.TokenResponse;
import roomescape.domain.auth.dto.UserInfoResponse;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.exception.InvalidAuthorizationException;
import roomescape.domain.auth.exception.UserNotFoundException;
import roomescape.domain.auth.repository.UserRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtManager jwtManager;
    private final UserRepository userRepository;
    private final PasswordEncryptor passwordEncryptor;
    private final String cookieKey;

    @Autowired
    public AuthService(final JwtManager jwtManager, final UserRepository userRepository,
                       final PasswordEncryptor passwordEncryptor, final JwtProperties jwtProperties) {
        this.jwtManager = jwtManager;
        this.userRepository = userRepository;
        this.passwordEncryptor = passwordEncryptor;
        this.cookieKey = jwtProperties.getCookieKey();
    }

    public TokenResponse login(final LoginRequest loginRequest) {
        final User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UserNotFoundException("해당 계정이 존재하지 않습니다."));

        user.login(loginRequest.email(), loginRequest.password(), passwordEncryptor);

        final String token = jwtManager.createToken(user);

        return new TokenResponse(token);
    }

    public UserInfoResponse getUserInfo(final LoginUserDto loginUserDto) {
        return userRepository.findById(loginUserDto.id())
                .map(UserInfoResponse::from)
                .orElseThrow(() -> new UserNotFoundException("해당 계정이 존재하지 않습니다."));
    }

    public LoginUserDto getLoginUser(final Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookieKey.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .map(this::getLoginUser)
                .orElseThrow(() -> new InvalidAuthorizationException("토큰이 존재하지 않습니다."));
    }

    public LoginUserDto getLoginUser(final String token) {
        if (!jwtManager.validateToken(token)) {
            log.error("잘못된 토큰입니다! token = {}", token);
            throw new InvalidAuthorizationException("잘못된 토큰입니다!");
        }

        final Long userId = jwtManager.parseUserId(token);

        return userRepository.findById(userId)
                .map(LoginUserDto::from)
                .orElseThrow(() -> new UserNotFoundException("해당 계정이 존재하지 않습니다."));
    }
}
