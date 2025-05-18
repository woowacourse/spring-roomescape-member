package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.UserDao;
import roomescape.dto.UserResponse;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<UserResponse> findAllUsers() {
        return userDao.findAll().stream()
                .map(UserResponse::fromEntity)
                .toList();
    }
}
