package roomescape.fake;

import roomescape.domain.model.Member;
import roomescape.domain.repository.MemberRepository;

import java.util.List;

public class FakeMemberRepository implements MemberRepository {

    private final FakeMemberDao memberDao;

    public FakeMemberRepository() {
        this.memberDao = new FakeMemberDao();
    }

    @Override
    public Long save(final Member member) {
        return memberDao.save(member);
    }

    @Override
    public Member findByEmail(final String email) {
        return memberDao.findByEmail(email);
    }

    @Override
    public Member findById(final Long memberId) {
        return memberDao.findById(memberId);
    }

    @Override
    public List<Member> findAll() {
        return memberDao.findAll();
    }

    @Override
    public boolean existByEmail(final String email) {
        return false;
    }
}
