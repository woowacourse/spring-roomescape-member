package roomescape.service.member;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.member.MemberDao;
import roomescape.domain.member.Member;
import roomescape.dto.member.request.MemberRegisterRequest;
import roomescape.dto.member.response.MemberRegisterResponse;
import roomescape.dto.member.response.MemberResponse;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberRegisterResponse register(final MemberRegisterRequest request) {
        final Member member = request.toEntity();
        final Long id = memberDao.save(member);
        return MemberRegisterResponse.from(id, member);
    }

    public List<MemberResponse> findAll() {
        final List<Member> members = memberDao.findAll();
        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }
}
