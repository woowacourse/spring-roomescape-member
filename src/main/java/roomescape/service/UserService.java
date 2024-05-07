package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.TokenProvider;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.service.dto.request.LoginRequest;
import roomescape.service.dto.response.MemberResponse;

@Service
public class UserService {

    private final MemberDao memberDao;
    private final TokenProvider tokenProvider;

    public UserService(MemberDao memberDao, TokenProvider tokenProvider) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberDao.findByEmailAndPassword(
                loginRequest.email(), 
                loginRequest.password());

        return tokenProvider.createToken(member);
    }

    public MemberResponse findMember(String token) {
        String payload = tokenProvider.getPayload(token);
        Member member = memberDao.findById(Long.valueOf(payload));

        return MemberResponse.from(member);
    }
}
