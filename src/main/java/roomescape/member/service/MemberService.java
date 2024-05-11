package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.MemberInfo;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberInfo> findAll() {
        return memberDao.findAll().stream()
                .map(member -> new MemberInfo(member.getId(), member.getName()))
                .toList();
    }
}
