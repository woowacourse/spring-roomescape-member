package roomescape.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;

@Repository
public class MemberJDBCRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> rowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getString("role")
    );

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, name, email, password, role FROM login_member WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, email));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(long id) {
        String sql = "SELECT id, name, email, password, role FROM login_member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT id, name, email, password, role FROM login_member";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
