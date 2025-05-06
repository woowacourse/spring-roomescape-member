package roomescape.persistence;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.User;
import roomescape.domain.UserRepository;

import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    public static final RowMapper<User> userRowMapper = (rs, rowNum) ->
            new User(rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByEmailAndPassword(final String email, final String password) {
        String sql = "SELECT id, name, email, password FROM users WHERE email = ? and password = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, email, password);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
