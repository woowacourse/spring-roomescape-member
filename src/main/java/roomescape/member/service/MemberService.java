package roomescape.member.service;

import org.springframework.stereotype.Service;

import roomescape.common.exception.EntityNotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findMemberByEmailAndPassword(final String email, final String password) {
        return memberDao.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new EntityNotFoundException("이메일 또는 패스워드가 잘못 되었습니다."));
    }
}
