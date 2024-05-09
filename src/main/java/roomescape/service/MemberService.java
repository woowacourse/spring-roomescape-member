package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.api.dto.request.MemberCreateRequest;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.exception.InvalidInputException;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member getMember(final MemberCreateRequest request) {
        final Member member = Member.of(null, null, request.email(), request.password());

        final var savedMember = memberDao.findByEmail(member)
                .orElseThrow(() -> new InvalidInputException("없는 이메일"));

        if (!savedMember.password().equals(member.password())) {
            throw new InvalidInputException("비밀번호 오류");
        }

        return savedMember;
    }
}
