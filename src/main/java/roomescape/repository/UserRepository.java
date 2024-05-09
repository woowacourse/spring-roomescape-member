package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.User;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (resultSet, rowNum) -> new User(
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password")
    );


    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByEmailAndPassword(String email, String password) {
        String sql = "SELECT exists(" +
                "SELECT 1 " +
                "FROM user " +
                "WHERE email = ? " +
                "AND password = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, email, password);
    }
}
