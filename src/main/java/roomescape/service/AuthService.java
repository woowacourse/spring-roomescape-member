package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.TokenProvider;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.exception.NotFoundException;
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

    public Member findLoginMember(String token) {
        String payload = tokenProvider.getPayload(token);
        return memberDao.findById(Long.valueOf(payload))
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }
}
