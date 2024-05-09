package roomescape.member.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MemberRepository {
    private static final RowMapper<Member> ROW_MAPPER = (resultSet, rowNum) ->
            new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberRepository(JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("MEMBER")
                .usingGeneratedKeyColumns("id");
    }

    public List<Member> readAll() {
        final String sql = """
                SELECT * FROM member""";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Member read(long memberId) {
        final String sql = """
                SELECT * FROM member WHERE id = ?""";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, memberId);
    }

    public Member read(String email) {
        final String sql = """
                SELECT * FROM member WHERE email = ?""";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, email);
    }

    public Long create(Member member) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword())
                .addValue("role", member.getRole());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }
}
