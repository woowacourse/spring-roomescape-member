package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.Password;
import roomescape.exception.CustomBadRequest;
import roomescape.service.dto.input.MemberCreateInput;
import roomescape.service.dto.input.MemberLoginInput;
import roomescape.service.dto.output.MemberOutput;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberOutput createMember(final MemberCreateInput input) {
        final var member = memberDao.create(input.toMember());
        return MemberOutput.from(member);
    }

    public List<MemberOutput> getAllMembers() {
        final var members = memberDao.getAll();
        return MemberOutput.list(members);
    }

    public MemberOutput getMember(final MemberLoginInput input) {
        final var member = memberDao.find(new Email(input.email()), new Password(input.password()))
                .orElseThrow(() -> new CustomBadRequest("없는 이메일이거나 잘못된 비밀번호입니다."));
        return MemberOutput.from(member);
    }

    public Member getMemberById(final Long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(() -> new CustomBadRequest(String.format("memberId(%s)가 존재하지 않습니다.", memberId)));
    }
}
