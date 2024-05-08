package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.user.Member;
import roomescape.service.dto.input.MemberCreateInput;
import roomescape.service.dto.output.MemberCreateOutput;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberCreateOutput createMember(final MemberCreateInput memberCreateInput) {
        final Member member = memberDao.create(memberCreateInput.toMember());
        return MemberCreateOutput.toOutput(member);
    }
}
