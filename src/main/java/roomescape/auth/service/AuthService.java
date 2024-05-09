package roomescape.auth.service;

import org.springframework.stereotype.Service;

import roomescape.auth.dao.UserDao;
import roomescape.auth.domain.Users;
import roomescape.auth.dto.UserLoginRequestDto;
import roomescape.auth.dto.UserSignUpRequestDto;
import roomescape.configuration.JwtTokenProvider;

@Service
public class AuthService {

    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final UserDao userDao, final JwtTokenProvider jwtTokenProvider) {
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(final UserLoginRequestDto userLoginRequestDto) {
        final Users users = userDao.getByEmailAndPassword(userLoginRequestDto.email(), userLoginRequestDto.password());
        final String token = jwtTokenProvider.createToken(users);
        return token;
    }

    public long signUp(final UserSignUpRequestDto userSignUpRequestDto) {
        return userDao.save(userSignUpRequestDto.toUsers());
    }
}
