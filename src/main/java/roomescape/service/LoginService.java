package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.UserDao;
import roomescape.dto.LoginRequest;
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
}
