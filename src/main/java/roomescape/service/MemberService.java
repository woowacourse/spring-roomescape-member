package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.MemberResponse;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse findById(final Long id) {
        final Member member = memberDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 사용자가 없습니다"));
        return new MemberResponse(member.getId(), member.getNameString(), member.getEmail(), member.getPassword());
    }
}
