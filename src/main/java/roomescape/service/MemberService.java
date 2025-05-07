package roomescape.service;

import org.springframework.stereotype.Component;
import roomescape.dao.MemberDao;
import roomescape.dto.LoginRequest;
import roomescape.entity.AccessToken;
import roomescape.entity.Member;

@Component
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void validateMemberExistence(LoginRequest login) {
        boolean hasMember = memberDao.existsByEmailAndPassword(login.email(), login.password());
        if (!hasMember) {
            //TODO : signup으로 넘어가나?
            throw new IllegalArgumentException("이메일 또는 비밀번호가 틀렸습니다.");
        }
    }

    public AccessToken createAccessToken(LoginRequest request) {
        Member member = memberDao.findByEmailAndPassword(request.email(), request.password());
        return new AccessToken(member);
    }
}
