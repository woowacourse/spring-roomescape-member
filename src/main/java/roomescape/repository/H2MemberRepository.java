package roomescape.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Role;

@Repository
public class H2MemberRepository implements MemberRepository {
    private final MemberRowMapper rowMapper;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public H2MemberRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.rowMapper = new MemberRowMapper();
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("user_table")
                .usingGeneratedKeyColumns("id");
    }

    public Member save(Member member) {
        Long memberId = jdbcInsert.executeAndReturnKey(Map.of(
                        "name", member.getName(),
                        "email", member.getEmail(),
                        "password", member.getPassword(),
                        "role", member.getRole().name()))
                .longValue();

        return new Member(
                memberId,
                member.getName(),
                member.getEmail(),
                member.getPassword(),
                member.getRole());
    }

    public boolean checkExistMember(String email, String password) {
        String sql = """
                SELECT 
                CASE WHEN EXISTS (
                        SELECT 1
                        FROM user_table
                        WHERE email = ? AND password = ?
                    )
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, email, password));
    }

    public Member findByEmail(String email) {
        String sql = """
                SELECT
                    id, name, email, password, role
                FROM
                    user_table
                WHERE
                    email = ?
                """;
        return jdbcTemplate.queryForObject(sql, rowMapper, email);
    }

    public Optional<Member> findById(Long memberId) {
        String sql = """
                SELECT
                    id, name, email, password, role
                FROM
                    user_table
                WHERE
                    id = ?
                """;
        Member member = jdbcTemplate.queryForObject(sql, rowMapper, memberId);
        return Optional.ofNullable(member);
    }

    public List<Member> findAll() {
        String sql = "SELECT * FROM user_table";
        return jdbcTemplate.query(sql, rowMapper);
    }

    private static class MemberRowMapper implements RowMapper<Member> {
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    Role.valueOf(rs.getString("role")));
        }
    }
}
