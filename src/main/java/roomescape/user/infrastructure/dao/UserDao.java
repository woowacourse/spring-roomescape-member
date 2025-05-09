package roomescape.user.infrastructure.dao;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.user.application.repository.UserRepository;
import roomescape.user.domain.Role;
import roomescape.user.domain.User;

@Repository
public class UserDao implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = """
                SELECT id, name, email, password, role FROM `user`
                WHERE email = ?
                """;
        try {
            User user = jdbcTemplate.queryForObject(sql,
                    (resultSet, rowNum) -> {
                        Role role = Role.valueOf(resultSet.getString("role"));
                        return new User(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("password"),
                                role
                        );
                    }, email);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
