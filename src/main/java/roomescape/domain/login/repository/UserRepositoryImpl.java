package roomescape.domain.login.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.login.domain.User;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<User> rowMapper = ((rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password")
    ));

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("escape_user")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public User insert(User user) {
        Map<String, Object> userRow = new HashMap<>();
        userRow.put("name", user.getName());
        userRow.put("email", user.getEmail());
        userRow.put("password", user.getPassword());
        Long id = simpleJdbcInsert.executeAndReturnKey(userRow).longValue();
        return new User(id, user.getName(), user.getEmail(), user.getPassword());
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "select * from escape_user where id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        String sql = "select * from escape_user where email = ? and password = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, email, password));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
