package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.service.dto.CreateMemberServiceRequest;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member addMember(final CreateMemberServiceRequest request) {
        validateEmailNotDuplicated(request.email());
        final Member member = Member.registerUser(request.name(), request.email(), request.password());

        final long savedId = memberDao.insert(member);
        return new Member(savedId, member);
    }

    private void validateEmailNotDuplicated(final String email) {
        if (memberDao.existsByEmail(email)) {
            throw new ExistedDuplicateValueException("이미 존재하는 이메일입니다: %s".formatted(email));
        }
    }

    public List<Member> findAllMembers() {
        return memberDao.findAll();
    }
}
