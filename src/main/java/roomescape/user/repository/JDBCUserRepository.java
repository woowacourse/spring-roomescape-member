package roomescape.user.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.user.domain.User;
import roomescape.user.entity.UserEntity;

@Repository
public class JDBCUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JDBCUserRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT id, name, email, password FROM user",
                (resultSet, rowNum) -> {
                    UserEntity entity = new UserEntity(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    );
                    return entity.toUser();
                }
        );
    }

    @Override
    public User put(final User user) {
        long generatedId = simpleJdbcInsert.executeAndReturnKey(
                Map.of("name", user.getName(),
                        "email", user.getEmail(),
                        "password", user.getPassword())).longValue();

        return User.of(generatedId, user.getName(), user.getEmail(), user.getPassword());
    }

    @Override
    public boolean deleteById(final long id) {
        return jdbcTemplate.update("DELETE FROM user WHERE id = ?", id) != 0;
    }

    @Override
    public Optional<User> findById(final long id) {
        try {
            UserEntity userEntity = jdbcTemplate.queryForObject(
                    "SELECT id, name, email, password FROM user WHERE id = ?",
                    (resultSet, rowNum) -> new UserEntity(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    ), id
            );
            return Optional.ofNullable(userEntity)
                    .map(UserEntity::toUser);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean checkExistsByEmailAndPassword(final String email, final String password) {
        Boolean exists = jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM user WHERE email = ? AND password = ?)",
                Boolean.class,
                email, password
        );
        return Boolean.TRUE.equals(exists);
    }
}
