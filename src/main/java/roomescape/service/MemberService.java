package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.exception.CustomBadRequest;
import roomescape.service.dto.input.MemberLoginInput;
import roomescape.service.dto.output.MemberOutput;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberOutput findMember(final MemberLoginInput input) {
        final var member = input.toMember();
        final var savedMember = memberDao.findByEmailAndPassword(member)
                .orElseThrow(() -> new CustomBadRequest("없는 이메일이거나 잘못된 비밀번호입니다."));
        return MemberOutput.from(savedMember);
    }

    public MemberOutput findMember(final Long id) {
        final var member = memberDao.findById(id)
                .orElseThrow(() -> new CustomBadRequest(String.format("멤버(id=%s)가 없습니다.", id)));
        return MemberOutput.from(member);
    }

    public List<MemberOutput> getAllMembers() {
        final var members = memberDao.getAll();
        return MemberOutput.list(members);
    }
}
