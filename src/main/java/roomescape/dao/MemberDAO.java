package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Role;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MemberDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberDAO(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Member insert(final Member member) {
        final String name = member.getName();
        final String email = member.getEmail();
        final String password = member.getPassword();
        final Role role = member.getRole();

        final SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("email", email)
                .addValue("password", password)
                .addValue("role", role.name().toLowerCase());

        final long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new Member(id, name, email, password, role);
    }

    public Boolean existMember(final String email, final String password) {
        final String sql = """
                SELECT CASE WHEN EXISTS (
                    SELECT 1
                    FROM member
                    WHERE email = ? AND password = ?
                ) THEN TRUE ELSE FALSE END;
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, email, password);
    }

    public Member findByEmail(final String email) {
        final String sql = "SELECT * FROM member WHERE email = ?;";
        return jdbcTemplate.queryForObject(sql, memberRowMapper(), email);
    }

    public Member findById(final Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper(), id);
    }

    public List<Member> findAll() {
        final String sql = "SELECT * FROM member";
        return jdbcTemplate.query(sql, memberRowMapper());
    }

    private RowMapper<Member> memberRowMapper() {
        return (resultSet, rowNum) -> new Member(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                Role.from(resultSet.getString("role"))
        );
    }
}
