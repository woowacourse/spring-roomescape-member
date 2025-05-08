package roomescape.user.repository;

import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import roomescape.user.domain.User;

@Repository
public class JdbcUserDao implements UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<User> userMapper = (resulSet, rowNum) ->
        new User(
                resulSet.getLong("id"),
                resulSet.getString("name"),
                resulSet.getString("email"),
                resulSet.getString("password")
        );

    public JdbcUserDao(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        final String sql = "SELECT * FROM users WHERE email = :email AND password = :password";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("password", password);
        User user = jdbcTemplate.queryForObject(sql, parameters, userMapper);
        return Optional.ofNullable(user);
    }
}
