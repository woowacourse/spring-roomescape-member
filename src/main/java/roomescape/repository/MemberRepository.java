package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;

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

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
}
