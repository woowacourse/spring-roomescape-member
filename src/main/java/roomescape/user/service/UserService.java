package roomescape.user.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomEscapeException.BadRequestException;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.infra.JwtTokenProvider;
import roomescape.user.domain.User;
import roomescape.user.dto.request.LoginRequest;
import roomescape.user.dto.response.BriefUserResponse;
import roomescape.user.dto.response.LoginCheckResponse;
import roomescape.user.repository.UserDao;

@Service
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDao userDao;

    public UserService(JwtTokenProvider jwtTokenProvider, UserDao userDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDao = userDao;
    }

    public String login(LoginRequest loginRequest) {
        User user = userDao.findByEmail(loginRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 이메일입니다."));
        if (!user.getPassword().equals(loginRequest.password())) {
            throw new BadRequestException("로그인에 실패하였습니다.");
        }
        try {
            return jwtTokenProvider.createAccessToken(loginRequest.email());
        } catch (Exception e) {
            throw new RuntimeException("cannot create token");
        }
    }

    public LoginCheckResponse checkUserByToken(String token) {
        if (!jwtTokenProvider.isValidToken(token)) {
            throw new RuntimeException("not a valid token");
        }
        String email = jwtTokenProvider.getPayload(token);
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 이메일입니다."));
        return new LoginCheckResponse(user.getName());
    }

    public List<BriefUserResponse> getAllUsers() {
        return userDao.findAll()
                .stream()
                .map(BriefUserResponse::from)
                .toList();
    }
}
