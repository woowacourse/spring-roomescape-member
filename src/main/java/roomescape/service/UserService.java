package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.UserDao;
import roomescape.domain.User;
import roomescape.dto.UserDto;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User register(UserDto userDto) {
        return userDao.insert(userDto.name(), userDto.email(), userDto.password());
    }

    public User login(String email, String password) {
        User user = userDao.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!user.isPasswordMatches(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
}
