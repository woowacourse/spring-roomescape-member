package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResonse;
import roomescape.repository.MemberDao;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResonse findBy(final MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(memberRequest.email());
        return MemberResonse.from(member);
    }
}
