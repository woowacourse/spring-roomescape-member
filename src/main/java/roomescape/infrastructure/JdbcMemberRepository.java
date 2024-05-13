package roomescape.infrastructure;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.member.Role;

@Repository
public class JdbcMemberRepository implements MemberRepository {
    private static final RowMapper<Member> ROW_MAPPER = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"),
            Role.valueOf(rs.getString("role"))
    );

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String query = """
                SELECT id, name, email, password, role FROM member
                WHERE email = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, ROW_MAPPER, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findById(long id) {
        String query = """
                SELECT id, name, email, password, role FROM member
                WHERE id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String query = """
                SELECT id, name, email, password, role FROM member
                """;
        return jdbcTemplate.query(query, ROW_MAPPER);
    }
}
