package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.ReservationMember;
import roomescape.member.dto.MemberResponse;
import roomescape.member.mapper.MemberMapper;

import java.util.List;

@Service
public class MemberService {

    private final MemberMapper memberMapper = new MemberMapper();
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberResponse> findAll() {
        List<ReservationMember> members = memberDao.findAll();

        return members.stream()
                .map(memberMapper::mapToResponse)
                .toList();
    }

    public ReservationMember findById(Long id) {
        return memberDao.findById(id);
    }
}
