package roomescape.repository.dao;

import java.sql.PreparedStatement;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import roomescape.domain.User;

@Component
@RequiredArgsConstructor
public class UserDao {

    private static final RowMapper<User> DEFAULT_ROW_MAPPER = (resultSet, rowNum) -> new User(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    private final JdbcTemplate jdbcTemplate;

    public User insertAndGet(User user) {
        String insertQuery = "INSERT INTO users (name, email, time) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[] {"id"});
            ps.setString(1, user.name());
            ps.setString(2, user.email());
            ps.setString(3, user.password());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();

        return user.withId(id);
    }

    public Optional<User> selectById(Long id) {
        String selectQuery = "SELECT id, name, email, password FROM users WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(selectQuery, DEFAULT_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> selectByEmail(String email) {
        String selectQuery = "SELECT id, name, email, password FROM users WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(selectQuery, DEFAULT_ROW_MAPPER, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
