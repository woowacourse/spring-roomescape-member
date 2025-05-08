package roomescape.infrastructure.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.model.Member;

import java.util.List;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Member> ROW_MAPPER = (resultSet, rowNum) ->
            new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            );

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member findByEmailAndPassword(String email, String password) {
        String query = "SELECT * FROM member WHERE email = ? and password = ?";
        return jdbcTemplate.queryForObject(
                query,
                ROW_MAPPER,
                email,
                password);
    }

    public Member findById(final Long memberId) {
        String query = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(
                query,
                ROW_MAPPER,
                memberId);
    }

    public List<Member> findAll() {
        String query = "SELECT * FROM member";
        return jdbcTemplate.query(query, ROW_MAPPER);
    }
}
