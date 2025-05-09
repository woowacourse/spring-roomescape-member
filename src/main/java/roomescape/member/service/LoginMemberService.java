package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.LoginMember;

@Service
public class LoginMemberService {
    private final MemberDao memberDao;

    public LoginMemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<LoginMember> findAllLoginMembers() {
        return memberDao.findAll();
    }
}
