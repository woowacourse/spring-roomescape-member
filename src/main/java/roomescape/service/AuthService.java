package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.common.exception.NotFoundException;
import roomescape.common.exception.UnauthorizedException;
import roomescape.dao.UserDao;
import roomescape.dto.request.LoginRequestDto;
import roomescape.model.User;

@Service
public class AuthService {

    private final UserDao userDao;

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void login(final LoginRequestDto loginRequestDto) {
        User user = findUserByEmail(loginRequestDto.email());
        if (!user.hasSamePassword(loginRequestDto.password())) {
            throw new UnauthorizedException("로그인에 실패했습니다.");
        }
    }

    private User findUserByEmail(final String email) {
        return userDao.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
    }
}
