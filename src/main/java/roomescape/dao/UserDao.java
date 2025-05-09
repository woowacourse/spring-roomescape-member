package roomescape.dao;

import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.model.User;

@Repository
public class UserDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<User> findByEmail(String email) {
        String sql = """
                SELECT id, name, email, password 
                FROM users
                WHERE email = :email
                """;

        try {
            User user = namedParameterJdbcTemplate.queryForObject(
                    sql,
                    Map.of("email", email),
                    (rs, rowNum) -> new User(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password")
                    )
            );
            return Optional.ofNullable(user);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
