package roomescape.user.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.user.model.Role;
import roomescape.user.model.User;

import java.sql.PreparedStatement;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long create(User user) {
        String sql = "INSERT INTO  `user` (name, role) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getRole().name());
                    return ps;
                }, keyHolder);
        return  keyHolder.getKey().longValue();
    }

    public User findById(Long id) {
        String sql = "SELECT * from `user` where id = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
            return new User(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    Role.valueOf(resultSet.getString("role"))
            );
        }, id);
    }
}
