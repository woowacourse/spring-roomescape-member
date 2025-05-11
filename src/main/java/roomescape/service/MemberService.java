package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.MemberRegisterRequest;
import roomescape.dto.MemberRegisterResponse;
import roomescape.dto.MemberResponse;

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
        List<Member> members = memberDao.findAll();
        return members.stream()
                .map(MemberResponse::from)
                .toList();
    }
}
