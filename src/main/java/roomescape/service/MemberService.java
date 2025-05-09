package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.request.MemberRequest;
import roomescape.exception.custom.DuplicatedException;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member addMember(MemberRequest request) {
        validateDuplicateMember(request);

        return memberDao.addMember(
            new Member(request.name(), request.email(), request.password()));
    }

    private void validateDuplicateMember(MemberRequest request) {
        if (memberDao.existMemberByEmail(request.email())) {
            throw new DuplicatedException("member");
        }
    }
}
