package roomescape.infrastructure.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import roomescape.domain.exception.UnauthorizedException;
import roomescape.domain.model.Member;
import roomescape.domain.repository.MemberRepository;
import roomescape.infrastructure.dao.MemberDao;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final MemberDao memberDao;

    public JdbcMemberRepository(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Member findByEmailAndPassword(final String email, final String password) {
        try {
            return memberDao.findByEmailAndPassword(email, password);
        } catch (EmptyResultDataAccessException e) {
            throw new UnauthorizedException();
        }
    }

    @Override
    public Member findById(final Long memberId) {
        try {
            return memberDao.findById(memberId);
        } catch (EmptyResultDataAccessException e) {
            throw new UnauthorizedException();
        }
    }
}
