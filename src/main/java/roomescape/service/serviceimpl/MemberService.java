package roomescape.service.serviceimpl;

import org.springframework.stereotype.Service;
import roomescape.repository.MemberDao;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }
}
