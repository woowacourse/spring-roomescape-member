package roomescape.business.service;

import org.springframework.stereotype.Service;
import roomescape.business.domain.User;
import roomescape.exception.ThemeNotFoundException;
import roomescape.exception.UserNotFoundException;
import roomescape.persistence.dao.UserDao;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(final UserDao userDao) {
        this.userDao = userDao;
    }

    public User find(final Long id) {
        return userDao.find(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
