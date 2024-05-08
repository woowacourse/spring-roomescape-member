package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;
import roomescape.repository.MemberDao;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse findBy(final MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(memberRequest.email());
        return MemberResponse.from(member);
    }

    public MemberResponse findById(final Long id) {
        Member member = memberDao.findById(id);
        return MemberResponse.from(member);
    }

    public Member findMemberById(final Long id) {
        return memberDao.findById(id);
    }
}
