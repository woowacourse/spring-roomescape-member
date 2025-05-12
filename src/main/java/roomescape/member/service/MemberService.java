package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.dao.MemberDao;
import roomescape.member.dto.response.MemberResponse;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberResponse> findAll() {

        List<Member> MemberDaoAll = memberDao.findAll();
        return MemberDaoAll.stream()
                .map(MemberResponse::toDto)
                .toList();
    }
}
