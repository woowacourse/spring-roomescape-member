package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.controller.member.dto.MemberResponseDto;
import roomescape.model.Member;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberResponseDto> findAllMember() {
        List<Member> members = memberDao.findAll();
        return members.stream()
                .map(MemberResponseDto::from)
                .toList();
    }
}
