package roomescape.domain.member.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.dao.MemberDao;
import roomescape.domain.member.dto.LoginRequest;
import roomescape.global.auth.AuthUser;
import roomescape.global.exception.RoomEscapeException;
import roomescape.global.jwt.JwtProvider;

@Service
public class LoginService {

    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public LoginService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    public String login(LoginRequest loginRequest) {
        AuthUser authUser = memberDao.findIdByEmailAndPassword(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new RoomEscapeException("[ERROR] 사용자를 찾을 수 없습니다."));

        return jwtProvider.createAccessToken(authUser);
    }
}
