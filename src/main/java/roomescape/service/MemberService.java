package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.dto.response.MemberResponse;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberResponse> getAll() {
        return memberDao.getAll()
            .stream()
            .map(MemberResponse::from)
            .toList();
    }
}
