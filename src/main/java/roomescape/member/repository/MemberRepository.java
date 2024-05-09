package roomescape.member.repository;

import org.springframework.stereotype.Repository;
import roomescape.global.exception.model.RoomEscapeException;
import roomescape.member.dao.MemberDao;
import roomescape.member.exception.MemberExceptionCode;

@Repository
public class MemberRepository {

    private final MemberDao memberDao;

    public MemberRepository(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public long findIdByEmailAndPassword(String email, String password) {
        return memberDao.findIdByEmailAndPassword(email, password)
                .orElseThrow(() -> new RoomEscapeException(MemberExceptionCode.ID_AND_PASSWORD_NOT_MATCH_OR_EXIST));
    }
}
