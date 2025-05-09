package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> findAllLoginMembers() {
        return memberDao.findAll();
    }
}
