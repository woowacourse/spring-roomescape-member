package roomescape.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.model.Role;
import roomescape.model.User;
import roomescape.model.UserName;

@Repository
public class UserDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<User> findByEmail(String email) {
        String sql = """
                SELECT id, name, email, password, role
                FROM users
                WHERE email = :email
                """;

        try {
            User user = namedParameterJdbcTemplate.queryForObject(
                    sql,
                    Map.of("email", email),
                    (rs, rowNum) -> new User(
                            rs.getLong("id"),
                            new UserName(rs.getString("name")),
                            rs.getString("email"),
                            rs.getString("password"),
                            Role.valueOf(rs.getString("role")))
            );
            return Optional.ofNullable(user);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findById(Long id) {
        String sql = """
                SELECT id, name, email, password, role
                FROM users
                WHERE id = :id
                """;
        try {
            User user = namedParameterJdbcTemplate.queryForObject(
                    sql,
                    Map.of("id", id),
                    (rs, rowNum) -> new User(
                            rs.getLong("id"),
                            new UserName(rs.getString("name")),
                            rs.getString("email"),
                            rs.getString("password"),
                            Role.valueOf(rs.getString("role"))
                    ));
            return Optional.ofNullable(user);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public List<User> findAll() {
        String sql = """
                SELECT id, name, email, password, role
                FROM users
                """;
        return namedParameterJdbcTemplate.query(
                sql,
                (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        new UserName(rs.getString("name")),
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role"))
                )
        );
    }
}
