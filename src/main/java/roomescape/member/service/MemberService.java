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
        return memberDao.findMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 맞는 회원은 존재하지 않습니다."));
    }

    public List<MemberReservationResponse> findAll() {
        return memberDao.findAllMemberReservationResponse();
    }

    public boolean isNotAdmin(long id) {
        Member member = findMemberById(id);
        return !member.isAdmin();
    }
}
