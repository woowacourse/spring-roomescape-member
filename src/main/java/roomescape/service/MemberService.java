package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;
import roomescape.repository.repositoryImpl.JdbcMemberDao;

@Service
public class MemberService {

    private final JdbcMemberDao memberDao;

    public MemberService(final JdbcMemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberResponse> findAll() {
        List<Member> allMembers = memberDao.findAll();
        return MemberResponse.fromMembers(allMembers);
    }

    public MemberResponse findBy(final MemberRequest memberRequest) {
        Member member = memberDao.findByEmail(memberRequest.email());
        return MemberResponse.from(member);
    }

    public MemberResponse findById(final Long id) {
        Member member = memberDao.findById(id);
        return MemberResponse.from(member);
    }

    public Member findMemberById(final Long id) {
        return memberDao.findById(id);
    }
}
