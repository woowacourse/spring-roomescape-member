package roomescape.user.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.vo.Role;
import roomescape.user.entity.User;

import java.util.Objects;

@Repository
public class UserJDBCDao implements UserRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public UserJDBCDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into users (email, password, name, role) values (:email, :password, :name, :role)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("name", user.getName())
                .addValue("role", user.getRole().toString());

        namedJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new User(id, user.getEmail(), user.getPassword(), user.getName(), user.getRole());
    }

    @Override
    public User findByEmail(String email) {
        String sql = "select * from users where email = :email";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email);

        return namedJdbcTemplate.queryForObject(sql, params, getUserRowMapper());
    }

    private RowMapper<User> getUserRowMapper() {
        return (resultSet, rowNum) -> new User(
                resultSet.getLong("id"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                Role.valueOf(resultSet.getString("role"))
        );
    }
}
