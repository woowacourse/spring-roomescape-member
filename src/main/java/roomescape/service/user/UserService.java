package roomescape.service.user;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.dao.user.UserDao;
import roomescape.domain.user.User;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(final UserDao userDao) {
        this.userDao = userDao;
    }

    public User findByEmail(final String email) {
        return userDao.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(
                        "[ERROR] (email : " + email + ") 에 대한 사용자가 존재하지 않습니다.")
                );
    }
}
