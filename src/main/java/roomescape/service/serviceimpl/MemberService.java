package roomescape.service.serviceimpl;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.response.MemberResponse;
import roomescape.repository.MemberDao;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse findMemberById(Long id) {
        return new MemberResponse(findMember(id));
    }

    private Member findMember(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("[ERROR] 멤버 id는 null일 수 없습니다.");
        }
        return memberDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 멤버를 찾을 수 없습니다."));
    }
}
