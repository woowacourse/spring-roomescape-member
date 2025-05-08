package roomescape.auth.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.auth.entity.User;

import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {
    private static final RowMapper<User> rowMapper = (resultSet, rowNum) ->
            new User(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"));
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        String query = "SELECT * FROM member WHERE email = :email AND password = :password";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email)
                .addValue("password", password);
        try {
            User user = jdbcTemplate.queryForObject(query, params, rowMapper);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String query = """
                SELECT
                    id, name, email, password
                FROM member
                WHERE email = :email
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email);
        try {
            User user = jdbcTemplate.queryForObject(query, params, rowMapper);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public User save(User user) {
        String query = "INSERT INTO member (name, email, password) VALUES (:name, :email, :password)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, params, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new User(
                id,
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );
    }
}
