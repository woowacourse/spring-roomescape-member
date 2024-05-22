package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.domain.Member;
import roomescape.repository.MemberDao;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> findAll() {
        return memberDao.findAll();
    }
}
