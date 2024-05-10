package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.api.dto.request.MemberCreateRequest;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.exception.InvalidInputException;
import roomescape.service.dto.output.MemberOutput;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findMember(final MemberCreateRequest request) {
        final Member member = Member.of(null, "임시 이름", request.email(), request.password(), "ADMIN");

        final var savedMember = memberDao.findByEmail(member)
                .orElseThrow(() -> new InvalidInputException("없는 이메일"));

        if (!savedMember.password().equals(member.password())) {
            throw new InvalidInputException("비밀번호 오류");
        }

        return savedMember;
    }

    public Member findMemberById(final String id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new InvalidInputException("없는 멤버 id"));
    }

    public List<MemberOutput> getAllMembers() {
        final var members = memberDao.getAll();
        return MemberOutput.list(members);
    }
}
