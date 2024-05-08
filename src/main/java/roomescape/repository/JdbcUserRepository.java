package roomescape.repository;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Role;
import roomescape.domain.User;
import roomescape.domain.UserName;
import roomescape.domain.UserRepository;

@Repository
public class JdbcUserRepository implements UserRepository {

    private static final RowMapper<User> USER_MAPPER = (resultSet, row) ->
            new User(
                    resultSet.getLong("id"),
                    new UserName(resultSet.getString("name")),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    Role.of(resultSet.getString("role"))
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, USER_MAPPER, email));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
