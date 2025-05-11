package roomescape.infrastructure.repository;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import roomescape.domain.exception.MemberDuplicatedException;
import roomescape.domain.exception.UnauthorizedException;
import roomescape.domain.model.Member;
import roomescape.domain.repository.MemberRepository;
import roomescape.infrastructure.dao.MemberDao;

import java.util.List;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberDao memberDao;

    public MemberRepositoryImpl(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Long save(final Member member) {
        try {
            return memberDao.save(member);
        } catch (DuplicateKeyException e) {
            throw new MemberDuplicatedException();
        }
    }

    @Override
    public boolean existByEmail(final String email) {
        return memberDao.existByEmail(email);
    }

    @Override
    public Member findByEmail(final String email) {
        try {
            return memberDao.findByEmail(email);
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

    @Override
    public List<Member> findAll() {
        return memberDao.findAll();
    }
}
