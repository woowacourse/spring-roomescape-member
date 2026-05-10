package roomescape.user.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.user.model.Role;
import roomescape.user.model.User;

import java.sql.PreparedStatement;
import java.util.Optional;

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

    public Optional<User> findByName(String name) {
        String sql = "SELECT id, name, role FROM \"USER\" WHERE name = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    Role.valueOf(rs.getString("role"))
            ), name);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
