package roomescape.user.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.user.domain.Role;
import roomescape.user.domain.User;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;
    private RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"),
            Role.toList(rs.getString("roles"))
    );

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<User> findByEmail(String email) {
        String sql = """
                SELECT id, name, email, password, roles
                    FROM users
                    WHERE email = ?
                """;
        try {
            User user = jdbcTemplate.queryForObject(
                    sql,
                    userRowMapper,
                    email
            );
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findById(Long id) {
        String sql = """
                SELECT id, name, email, password, roles
                FROM users
                WHERE id = ?
                """;
        try {
            User user = jdbcTemplate.queryForObject(
                    sql,
                    userRowMapper,
                    id
            );
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<User> findAll() {
        String sql = """
                SELECT id, name, email, password, roles
                FROM users 
                """;
        return jdbcTemplate.query(
                sql,
                userRowMapper
        );
    }
}
