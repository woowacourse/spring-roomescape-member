package roomescape.user.infrastructure;

import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public boolean existedByEmailAndPassword(String email, String password) {
        String sql = "SELECT EXISTS (SELECT 1 FROM users WHERE email = :email AND password = :password)";

        Map<String, Object> parameter = Map.of("email", email,
                                            "password", password);

        return Boolean.TRUE.equals(
                namedParameterJdbcTemplate.queryForObject(sql, parameter, Boolean.class));
    }
}
