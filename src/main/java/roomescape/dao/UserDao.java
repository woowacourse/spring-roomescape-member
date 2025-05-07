package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.model.User;

@Repository
public class UserDao {

    private final RowMapper<User> userRowMapper = (resultSet, rowNum) -> {
        return new User(
                resultSet.getString("email"),
                resultSet.getString("name"),
                resultSet.getString("password"));
    };

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByEmailAndPassword(User user){
        String sql = "SELECT EXISTS(SELECT 1 FROM user WHERE email = ? AND password = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, user.getEmail(), user.getPassword()));
    }
}
