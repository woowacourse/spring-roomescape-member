package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.dao.UserDao;
import roomescape.dto.LoginRequest;
import roomescape.dto.UserResponse;
import roomescape.infra.JwtTokenProvider;
import roomescape.model.User;

@Service
public class LoginService {
    private final UserDao userDao;

    public LoginService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User login(LoginRequest loginRequest) {
        User user = userDao.findByEmail(loginRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if(!user.getPassword().equals(loginRequest.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    public UserResponse findUserFromToken(Cookie[] cookies) {
        String token = JwtTokenProvider.extractTokenFromCookies(cookies);
        Long id = JwtTokenProvider.getUserId(token);
        User user = userDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 ID가 없습니다."));
        return UserResponse.from(user);
    }
}
