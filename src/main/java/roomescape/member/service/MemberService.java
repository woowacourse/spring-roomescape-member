package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.dao.MemberDao;
import roomescape.member.dto.MemberResponse;
import roomescape.member.dto.MembersResponse;

import java.util.List;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MembersResponse findAllMembers() {
        List<MemberResponse> response = memberDao.findAll().stream()
                .map(MemberResponse::fromEntity)
                .toList();

        return new MembersResponse(response);
    }
}
