package roomescape.user.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.user.domain.User;
import roomescape.user.dto.response.UserSelectElementResponse;
import roomescape.user.repository.UserDao;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User findUserByEmail(String email) {
        return userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 이메일입니다."));
    }

    public User getUserById(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 사용자입니다."));
    }

    public List<UserSelectElementResponse> getAllUsers() {
        return userDao.findAll()
                .stream()
                .map(UserSelectElementResponse::from)
                .toList();
    }
}
