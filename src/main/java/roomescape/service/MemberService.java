package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findMember(LoginRequest loginRequest) {
        Member member = new Member(loginRequest.email(), loginRequest.password());
        return memberDao.find(member);
    }
}
