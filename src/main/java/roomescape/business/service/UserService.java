package roomescape.business.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.business.domain.User;
import roomescape.exception.UserNotFoundException;
import roomescape.persistence.dao.UserDao;
import roomescape.presentation.dto.UserResponse;

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

    public List<UserResponse> findAll() {
        return userDao.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }
}
