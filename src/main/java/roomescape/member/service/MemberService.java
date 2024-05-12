package roomescape.member.service;

import org.springframework.stereotype.Service;

import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findMemberById(long id) {
        return memberDao.findMemberById(id);
    }

    public String findMemberNameById(long id) {
        return memberDao.findNameById(id);
    }

}
