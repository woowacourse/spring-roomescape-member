package roomescape.user.service;

import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomEscapeException.AuthenticationException;
import roomescape.global.exception.RoomEscapeException.BadRequestException;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.infra.JwtTokenProvider;
import roomescape.user.domain.User;
import roomescape.user.dto.request.LoginRequest;
import roomescape.user.dto.response.LoginCheckResponse;
import roomescape.user.repository.UserDao;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDao userDao;
    private final UserService userService;

    public AuthService(JwtTokenProvider jwtTokenProvider, UserDao userDao, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDao = userDao;
        this.userService = userService;
    }

    public String login(LoginRequest loginRequest) {
        User user = userService.findUserByEmail(loginRequest.email());
        if (!user.getPassword().equals(loginRequest.password())) {
            throw new BadRequestException("로그인에 실패하였습니다.");
        }
        try {
            return jwtTokenProvider.createAccessToken(loginRequest.email());
        } catch (Exception e) {
            throw new AuthenticationException("토큰을 생성하지 못하였습니다.");
        }
    }

    public LoginCheckResponse checkUserByToken(String token) {
        if (!jwtTokenProvider.isValidToken(token)) {
            throw new AuthenticationException("유효하지 않은 토큰입니다.");
        }
        String email = jwtTokenProvider.getPayload(token);
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 이메일입니다."));
        return new LoginCheckResponse(user.getName());
    }
}
