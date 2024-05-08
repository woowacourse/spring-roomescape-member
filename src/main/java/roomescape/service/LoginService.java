package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.LoginDao;
import roomescape.domain.User;
import roomescape.dto.LoginDto;

@Service
public class LoginService {

    private final LoginDao loginDao;

    public LoginService(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    public User addUser(LoginDto loginDto) {
        return loginDao.insert(loginDto.name(), loginDto.email(), loginDto.password());
    }

    public User login(String email, String password) {
        User user = loginDao.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!user.isPasswordMatches(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
}
