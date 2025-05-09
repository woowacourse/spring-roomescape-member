package roomescape.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.application.dto.MemberResponse;
import roomescape.dao.MemberDao;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberResponse> findAllMembers() {
        return memberDao.findAll().stream()
                .map(MemberResponse::new)
                .toList();
    }
}
