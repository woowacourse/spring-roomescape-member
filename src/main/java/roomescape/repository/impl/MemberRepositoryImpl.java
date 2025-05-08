package roomescape.repository.impl;

import org.springframework.stereotype.Component;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.repository.MemberRepository;

@Component
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberDao memberDao;

    public MemberRepositoryImpl(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void save(Member member) {
        long savedId = memberDao.save(member);
        member.setId(savedId);
    }

    @Override
    public Member findByEmailAndPassword(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
}
