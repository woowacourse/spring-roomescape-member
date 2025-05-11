package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.entity.Member;
import roomescape.entity.Role;

import java.util.List;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private static final RowMapper<Member> ROWMAPPER =
            (resultSet, rowNum) -> Member.afterSave(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    Role.valueOf(resultSet.getString("role"))
            );


    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Member> findAll() {
        final String sql = "SELECT * FROM member";
        return jdbcTemplate.query(sql, ROWMAPPER);
    }

    @Override
    public Member findById(final long memberId) {
        try {
            final String sql = "SELECT * FROM member WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, ROWMAPPER, memberId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
