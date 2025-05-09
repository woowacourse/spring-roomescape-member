package roomescape.user.service;

import org.springframework.stereotype.Service;
import roomescape.user.domain.User;
import roomescape.user.repository.UserDao;
import roomescape.user.service.dto.LoginInfo;
import roomescape.user.service.dto.TokenResponse;
import roomescape.user.service.dto.UserInfo;

@Service
public class AuthService {

    private final UserDao userDao;
    private final TokenProvider tokenProvider;

    public AuthService(final UserDao userDao, final TokenProvider tokenProvider) {
        this.userDao = userDao;
        this.tokenProvider = tokenProvider;
    }

    public TokenResponse tokenLogin(final LoginInfo loginInfo) {
        final User loginUser = userDao.findByEmailAndPassword(loginInfo.email(), loginInfo.password())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일 혹은 비밀번호입니다."));
        String token = tokenProvider.createToken(loginUser.getEmail());
        return new TokenResponse(token);
    }

    public UserInfo getUserInfoByToken(String token) {
        String email = tokenProvider.parsePayload(token);
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 정보입니다."));
        return new UserInfo(user);
    }
}
