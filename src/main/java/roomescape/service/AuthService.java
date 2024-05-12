package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.exception.NotFoundException;
import roomescape.service.dto.AuthInfo;
import roomescape.service.dto.request.LoginRequest;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final TokenProvider tokenProvider;

    public AuthService(MemberDao memberDao, TokenProvider tokenProvider) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(
                        loginRequest.email(),
                        loginRequest.password())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        return tokenProvider.createToken(member);
    }

    public AuthInfo getAuthInfo(Cookie[] cookies) {
        return tokenProvider.getAuthInfo(cookies);
    }
}
