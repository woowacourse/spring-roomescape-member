package roomescape.auth.service;

import org.springframework.stereotype.Service;

import roomescape.auth.dao.UserDao;
import roomescape.auth.domain.Users;
import roomescape.auth.dto.UserRequestDto;
import roomescape.configuration.JwtTokenProvider;

@Service
public class AuthService {

    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final UserDao userDao, final JwtTokenProvider jwtTokenProvider) {
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(final UserRequestDto userRequestDto) {
        final Users users = userDao.getByEmailAndPassword(userRequestDto.email(), userRequestDto.password());
        String token = jwtTokenProvider.createToken(users);
        return token;
    }
}
