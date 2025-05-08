package roomescape.fake;

import roomescape.domain.model.Member;
import roomescape.domain.repository.MemberRepository;

public class FakeMemberRepository implements MemberRepository {

    private final FakeMemberDao memberDao;

    public FakeMemberRepository() {
        this.memberDao = new FakeMemberDao();
    }

    @Override
    public Member findByEmailAndPassword(final String email, final String password) {
        return memberDao.findByEmailAndPassword(email, password);
    }

    @Override
    public Member findById(final Long memberId) {
        return memberDao.findById(memberId);
    }
}
