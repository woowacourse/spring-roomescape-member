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
        String sql = "SELECT * FROM _user WHERE email = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, userRowMapper, email));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    // TODO: 여기서 USER 를 매개변수로 바로 반환하는 게 맞나? SimpleJdbcInsert 사용하면 좋을 듯
    public User save(final User user) {
        String sql = "INSERT INTO _user VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getNameValue(), user.getEmail(), user.getPassword());
        return user;
    }
}
