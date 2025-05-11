package roomescape.persistence;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.business.Member;

@Repository
public class H2MemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> (
            new Member(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
            )
    );

    @Override
    public Optional<Member> findByEmail(String email) {
        String query = """
                SELECT id, name, email, password
                FROM member
                WHERE email = ?
                """;
        return jdbcTemplate.query(query, memberRowMapper, email)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Member> findById(Long id) {
        String query = """
                SELECT id, name, email, password
                FROM member
                WHERE id = ?
                """;
        return jdbcTemplate.query(query, memberRowMapper, id)
                .stream()
                .findFirst();
    }
}
