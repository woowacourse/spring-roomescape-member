package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;

import javax.sql.DataSource;

@Repository
public class MemberRepository {

    private static final RowMapper<Member> ROW_MAPPER = (resultSet, rowNum) -> new Member(
            resultSet.getLong("member.id"),
            resultSet.getString("member.name"),
            resultSet.getString("member.role"),
            resultSet.getString("member.email"),
            resultSet.getString("member.password")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Member findById(Long id) {
        String sql = """
                SELECT * FROM member m 
                WHERE m.id = ? 
                """;

        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
    }

    public Member findByEmailAndPassword(String email, String password) {
        String sql = """
                SELECT * FROM member m 
                WHERE m.email = ? AND m.password = ? 
                """;

        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, email, password);
    }

    public Member save(Member requestMember) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", requestMember.getName())
                .addValue("role", requestMember.getRole())
                .addValue("email", requestMember.getEmail())
                .addValue("password", requestMember.getPassword());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Member(
                id,
                requestMember.getName(),
                requestMember.getRole(),
                requestMember.getEmail(),
                requestMember.getPassword()
        );
    }
}
