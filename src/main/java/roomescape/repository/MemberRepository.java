package roomescape.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import roomescape.domain.Member;
import roomescape.repository.dao.MemberDao;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final MemberDao memberDao;

    public Member save(Member member) {
        return memberDao.insertAndGet(member);
    }

    public Optional<Member> findById(Long id) {
        return memberDao.selectById(id);
    }

    public Member getById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    public Optional<Member> findByEmail(String email) {
        return memberDao.selectByEmail(email);
    }

    public List<Member> findAll() {
        return memberDao.selectAll();
    }
}
