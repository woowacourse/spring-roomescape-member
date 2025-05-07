package roomescape.infrastructure.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.model.Member;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Member> ROW_MAPPER = (resultSet, rowNum) ->
            new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member findByEmailAndPassword(String email, String password) {
        String query = "SELECT id, name, email, password FROM member WHERE email = ? and password = ?";
        return jdbcTemplate.queryForObject(
                query,
                ROW_MAPPER,
                email,
                password);
    }
}
