package roomescape.service;

import org.springframework.stereotype.Service;

import roomescape.controller.request.UserLoginRequest;
import roomescape.exception.AuthorizationException;
import roomescape.model.User;
import roomescape.repository.UserDao;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User findUserByEmailAndPassword(UserLoginRequest request) {
        return userDao.findUserByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new AuthorizationException(
                        "사용자(email: %s, password: %s)가 존재하지 않습니다.".formatted(request.email(), request.password())));
    }
}
