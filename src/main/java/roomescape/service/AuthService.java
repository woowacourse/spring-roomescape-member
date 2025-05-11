package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.dao.UserDao;
import roomescape.dto.LoginRequest;
import roomescape.dto.LoginCheckResponse;
import roomescape.infra.JwtTokenProvider;
import roomescape.model.User;

@Service
public class AuthService {
    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserDao userDao, JwtTokenProvider jwtTokenProvider) {
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String loginAndGetToken(LoginRequest loginRequest) {
        User user = login(loginRequest);
        return jwtTokenProvider.createToken(user.getId(), user.getNameValue(), user.getRole().name());
    }

    private User login(LoginRequest loginRequest) {
        User user = userDao.findByEmail(loginRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if(!user.getPassword().equals(loginRequest.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    public LoginCheckResponse findUserFromToken(Cookie[] cookies) {
        String token = jwtTokenProvider.extractTokenFromCookies(cookies);
        Long id = jwtTokenProvider.getUserId(token);
        User user = findById(id);
        return LoginCheckResponse.from(user);
    }

    public String extractTokenFromCookies(Cookie[] cookies) {
        return jwtTokenProvider.extractTokenFromCookies(cookies);
    }

    public Long getUserId(String token) {
        return jwtTokenProvider.getUserId(token);
    }

    public User findById(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 ID가 없습니다."));
    }
}
