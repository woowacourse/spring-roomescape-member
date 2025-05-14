package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.dto.request.LoginRequest;
import roomescape.entity.AccessToken;
import roomescape.entity.Member;

@Service
public class AuthorizationService {

    private final MemberDao memberDao;

    public AuthorizationService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void validateMemberExistence(LoginRequest login) {
        boolean hasMember = memberDao.existsByEmailAndPassword(login.email(), login.password());
        if (!hasMember) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다.");
        }
    }

    public AccessToken createAccessToken(LoginRequest request) {
        Member member = memberDao.findByEmailAndPassword(request.email(), request.password());
        return new AccessToken(member);
    }
}
