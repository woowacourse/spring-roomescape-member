package roomescape.repository.member;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class H2MemberRepository implements MemberRepository {

    private static final RowMapper<Member> mapper;
    private final JdbcTemplate template;

    static {
        mapper = (resultSet, resultNumber) -> new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    }

    public H2MemberRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE member.email = ?";
        try {
            Member member = template.queryForObject(sql, mapper, email);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
