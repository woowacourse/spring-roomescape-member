package roomescape.infrastructure;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.model.entity.User;
import roomescape.business.model.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private static final RowMapper<User> ROW_MAPPER = (resultSet, rowNum) -> {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        return User.afterSave(id, name, email, password);
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    public JdbcUserRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public User saveAndGet(final User user) {
        final Number id = insert.executeAndReturnKey(Map.of(
                "email", user.email(),
                "name", user.name(),
                "password", user.password()
        ));

        return User.afterSave(id.longValue(), user.name(), user.email(), user.password());
    }

    @Override
    public List<User> findAll() {
        final String sql = """
                SELECT * FROM users
                """;

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public Optional<User> findById(final long userId) {
        final String sql = """
                SELECT * FROM users
                WHERE id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, userId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        final String sql = """
                SELECT * FROM users
                WHERE email = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existByEmail(final String email) {
        final String sql = """
                SELECT COUNT(*) FROM users
                WHERE email = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }
}
