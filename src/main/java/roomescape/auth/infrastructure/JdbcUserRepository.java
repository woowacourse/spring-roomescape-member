package roomescape.auth.infrastructure;

import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.auth.domain.User;
import roomescape.auth.domain.UserRepository;

@Repository
public class JdbcUserRepository implements UserRepository {
    private final static RowMapper<User> USER_ROW_MAPPER =
            (rs, rowNum) -> new User(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name")
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users");

        Map<String, Object> parameters = Map.of(
                "email", user.getEmail(),
                "password", user.getPassword(),
                "name", user.getName()
        );

        simpleJdbcInsert.execute(parameters);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String selectOneSql = "SELECT email, password, name FROM users WHERE email=?";
        try {
            User user = jdbcTemplate.queryForObject(selectOneSql, USER_ROW_MAPPER,
                    email);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
