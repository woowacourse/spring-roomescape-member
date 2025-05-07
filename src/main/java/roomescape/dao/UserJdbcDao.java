package roomescape.dao;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.model.User;

@Repository
public class UserJdbcDao implements UserDao {

    private static final RowMapper<User> USER_ROW_MAPPER = (resultSet, rowNum) ->
            new User(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findByEmail(final String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.query(sql, USER_ROW_MAPPER, email).stream().findAny();
    }
}
