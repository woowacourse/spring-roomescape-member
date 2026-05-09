package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.User;

@Component
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> rowMapper = (resultSet, rowNum) -> new User(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email")
    );

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findById(Long id) {
        String sql = "SELECT id, name, email FROM user WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
}
