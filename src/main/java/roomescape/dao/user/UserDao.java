package roomescape.dao.user;

import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.user.User;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper;

    public UserDao(final DataSource dataSource, final RowMapper<User> userRowMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.userRowMapper = userRowMapper;
    }

    public Optional<User> findByEmail(final String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, userRowMapper, email));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
