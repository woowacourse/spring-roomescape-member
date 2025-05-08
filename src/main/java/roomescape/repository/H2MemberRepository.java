package roomescape.repository;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class H2MemberRepository implements MemberRepository {

    private static final RowMapper<Member> mapper;

    static {
        mapper = (resultSet, resultNumber) -> new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("role")
        );
    }

    private final JdbcTemplate template;

    public H2MemberRepository(final JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<Member> findMember(String email, String password) {
        String sql =
                """ 
                        SELECT * 
                        FROM member
                        WHERE email = ?
                        AND password = ?
                        """;
        try {
            Member member = template.queryForObject(sql, mapper, email, password);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
