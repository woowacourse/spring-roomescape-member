package roomescape.service.member;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.member.MemberDao;
import roomescape.domain.member.MemberInfo;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberInfo> findAll() {
        return memberDao.findAll();
    }
}
