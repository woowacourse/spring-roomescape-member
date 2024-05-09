package roomescape.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.domain.User;
import roomescape.domain.repository.UserRepository;

@Repository
public class UserDao implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<User> userRowMapper = (resultSet, rowNum) -> new User(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );

    public UserDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> findUsers = jdbcTemplate.query(sql, userRowMapper, id);
        return DataAccessUtils.optionalResult(findUsers);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        List<User> findUsers = jdbcTemplate.query(sql, userRowMapper, email, password);
        return DataAccessUtils.optionalResult(findUsers);
    }

    @Override
    public User save(User user) {
        Map<String, String> sqlParams = Map.of("name", user.getName(),
                "email", user.getEmail(),
                "password", user.getPassword());
        Long id = jdbcInsert.executeAndReturnKey(sqlParams)
                .longValue();
        return new User(id, user.getName(), user.getEmail(), user.getPassword());
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
    }
}
