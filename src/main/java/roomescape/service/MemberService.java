package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberCheckResponse;
import roomescape.dto.MemberResponse;
import roomescape.repository.repositoryImpl.JdbcMemberDao;

@Service
public class MemberService {

    private final JdbcMemberDao memberDao;

    public MemberService(final JdbcMemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberCheckResponse> findAll() {
        List<Member> allMembers = memberDao.findAll();
        return MemberCheckResponse.fromMembers(allMembers);
    }

    public MemberResponse findBy(final MemberRequest memberRequest) {
        Member member = memberDao.findByEmailAndPassword(memberRequest.email(), memberRequest.password());
        return MemberResponse.from(member);
    }

    public MemberCheckResponse findById(final Long id) {
        Member member = memberDao.findById(id);
        return MemberCheckResponse.from(member);
    }
}
