package roomescape.user.infrastructure;

import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.user.domain.User;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<Long> findIdByEmailAndPassword(String email, String password) {
        String sql = "SELECT id FROM users WHERE email = :email AND password = :password";

        Map<String, Object> parameter = Map.of("email", email,
                                            "password", password);

        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(
                    sql,
                    parameter,
                    (resultSet, rowNum) -> resultSet.getLong("id")));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = :id";

        Map<String, Object> parameter = Map.of("id", id);

        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(
                    sql,
                    parameter,
                    (resultSet, rowNum) -> new User(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"))));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
