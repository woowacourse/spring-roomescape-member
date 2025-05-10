package roomescape.service.fake_dao;

import java.util.List;
import roomescape.dao.MemberDao;
import roomescape.entity.Member;

public class FakeMemberDao implements MemberDao {

    @Override
    public boolean existsByEmailAndPassword(String email, String password) {
        return true;
    }

    @Override
    public Member findByEmailAndPassword(String email, String password) {
        return null;
    }

    @Override
    public Member create(Member member) {
        return null;
    }

    @Override
    public Member findById(long memberId) {
        return null;
    }

    @Override
    public List<Member> findAll() {
        return List.of();
    }
}
