package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.member.Member;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.member.MemberSignupRequest;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse add(MemberSignupRequest signupRequest) {
        Member member = signupRequest.toDomain();
        Member createdMember = memberDao.create(member);
        return new MemberResponse(createdMember);
    }
}
