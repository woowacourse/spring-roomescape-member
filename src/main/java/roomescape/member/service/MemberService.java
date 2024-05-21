package roomescape.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;
import roomescape.member.response.MemberReservationResponse;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findMemberById(long id) {
        return memberDao.findMemberById(id);
    }

    public List<MemberReservationResponse> findAll() {
        return memberDao.findAllReservationResponse();
    }

    public boolean isAdmin(long id) {
        Member member = memberDao.findMemberById(id);
        return member.isAdmin();
    }
}
