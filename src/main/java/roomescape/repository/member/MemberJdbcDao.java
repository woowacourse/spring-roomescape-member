package roomescape.repository.member;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.entity.Member;

@Repository
public class MemberJdbcDao implements MemberRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public MemberJdbcDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public Member findById(Long id) {
        return null;
    }

    @Override
    public Member findByEmailAndPassword(String email, String password) {
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }
}
