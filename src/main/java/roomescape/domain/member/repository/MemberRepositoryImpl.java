package roomescape.domain.member.repository;

import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.domain.member.dao.MemberDao;
import roomescape.domain.member.model.Member;

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
    public Member findByEmail(String email) {
        return memberDao.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    @Override
    public Member findByEmailAndPassword(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    @Override
    public Member findById(Long id) {
        return memberDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    @Override
    public List<Member> findAll() {
        return memberDao.findAll();
    }
}
