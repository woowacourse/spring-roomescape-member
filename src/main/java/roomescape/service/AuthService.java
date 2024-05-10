package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.exception.NotFoundException;
import roomescape.service.dto.LoginMember;
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

    public LoginMember findLoginMember(Cookie[] cookies) {
        String token = tokenProvider.extractTokenBy(cookies);

        String payload = tokenProvider.getPayload(token);
        Member member = memberDao.findById(Long.valueOf(payload))
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
        return new LoginMember(member.getId(), member.getName(), member.getRole());
    }
}
