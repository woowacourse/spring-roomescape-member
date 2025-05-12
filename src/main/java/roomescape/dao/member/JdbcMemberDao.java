package roomescape.dao.member;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;

@Repository
public class JdbcMemberDao implements MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> memberMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            MemberRole.valueOf(resultSet.getString("role"))
    );

    public JdbcMemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        final String sql = """
                SELECT *
                FROM member
                WHERE email = ?
                """;
        return jdbcTemplate.query(sql, memberMapper, email).stream()
                .findFirst();
    }

    @Override
    public Optional<Member> findById(final Long id) {
        final String sql = """
                SELECT *
                FROM member
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, memberMapper, id).stream()
                .findFirst();
    }

    @Override
    public List<Member> findAll() {
        final String sql = """
                SELECT * 
                FROM member
                """;
        return jdbcTemplate.query(sql, memberMapper);
    }

    @Override
    public boolean existsById(final Long id) {
        final String sql = """
                SELECT COUNT(*)
                FROM member
                WHERE id = ?
                """;
        final Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
