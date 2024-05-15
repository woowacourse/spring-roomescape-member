package roomescape.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Email;
import roomescape.domain.Member;
import roomescape.domain.MemberName;
import roomescape.domain.MemberRole;
import roomescape.domain.Password;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findByEmail(String email) {
        String sql = """
                SELECT 
                    id,
                    name,
                    role,
                    email,
                    password
                FROM member
                WHERE email = ?
                LIMIT 1
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, getMemberRowMapper(), email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(Long id) {
        String sql = """
                SELECT 
                    id,
                    name,
                    role,
                    email,
                    password
                FROM member
                WHERE id = ?
                LIMIT 1
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, getMemberRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Member> findAll() {
        return jdbcTemplate.query("SELECT id, name, role, email, password FROM member", getMemberRowMapper());
    }

    private RowMapper<Member> getMemberRowMapper() {
        return (resultSet, rowNum) -> new Member(
                resultSet.getLong("id"),
                new MemberName(resultSet.getString("name")),
                new Email(resultSet.getString("email")),
                new Password(resultSet.getString("password")),
                MemberRole.from(resultSet.getString("role"))
        );
    }
}
