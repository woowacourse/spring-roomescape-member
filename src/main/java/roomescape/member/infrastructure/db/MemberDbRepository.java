package roomescape.member.infrastructure.db;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

@Repository
@RequiredArgsConstructor
public class MemberDbRepository implements MemberRepository {

    private final MemberDao memberDao;

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        return memberDao.selectByEmailAndPassword(email, password);
    }

    @Override
    public List<Member> getAll() {
        return memberDao.getAll();
    }
}
