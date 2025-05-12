package roomescape.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.common.Role;
import roomescape.model.Member;

@Repository
public class MemberDao {

    private final RowMapper<Member> customerRowMapper = (resultSet, rowNum) -> {
        return new Member(
                resultSet.getLong("id"),
                resultSet.getString("email"),
                resultSet.getString("name"),
                resultSet.getString("password"),
                Role.fromMemberRole(resultSet.getString("role")));
    };

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Member> findAll() {
        String sql = "SELECT id, email, name, password, role FROM member";
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, email, name, password, role FROM member WHERE email = ? AND password = ?";
        return jdbcTemplate.query(sql, customerRowMapper, email, password)
                .stream()
                .findFirst();
    }

    public Optional<Member> findById(Long customerId) {
        String sql = "SELECT id, email, name, password, role FROM member WHERE id = ?";
        return jdbcTemplate.query(sql, customerRowMapper, customerId)
                .stream().
                findFirst();
    }
}
