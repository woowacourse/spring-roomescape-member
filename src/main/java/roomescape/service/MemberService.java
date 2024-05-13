package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.MemberRequest;
import roomescape.dto.response.MemberResponse;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberResponse> findAll() {
        return memberDao.findAll()
                .stream()
                .map(MemberResponse::fromMember)
                .toList();
    }

    public Member findMember(LoginRequest loginRequest) {
        Member member = new Member(loginRequest.email(), loginRequest.password());
        return memberDao.find(member);
    }

    public Member findMember(MemberRequest memberRequest) {
        return memberDao.findById(memberRequest.id());
    }
}
