package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.UserDao;
import roomescape.dto.user.LoginRequestDto;
import roomescape.model.User;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void verifyLogin(LoginRequestDto loginRequestDto){
        User user = loginRequestDto.convertToUser();
        boolean isExists = userDao.existsByEmailAndPassword(user);
    }
}
