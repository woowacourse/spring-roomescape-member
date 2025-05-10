package roomescape.user.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.jdbc.JdbcUtils;
import roomescape.user.domain.User;
import roomescape.user.domain.UserId;
import roomescape.user.domain.UserRepository;
import roomescape.user.domain.UserRole;

import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcTemplateUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (resultSet, rowNum) ->
            User.withId(
                    UserId.from(resultSet.getLong("id")),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    UserRole.valueOf(resultSet.getString("role"))
            );

    @Override
    public Optional<User> findByEmail(final String email) {
        final String sql = """
                SELECT id, name, email, password, role
                FROM users
                WHERE email = ?
                """;
        return JdbcUtils.queryForOptional(jdbcTemplate, sql, userRowMapper, email);
    }

    @Override
    public Optional<String> findPasswordByEmail(final String email) {
        final String sql = """
                SELECT password
                FROM users
                WHERE email = ?
                """;
        final Optional<String> password = JdbcUtils.queryForOptional(
                jdbcTemplate,
                sql,
                (resultSet, rowNum) -> resultSet.getString("password"),
                email);
        return password;
    }

    @Override
    public User save(final User user) {
        final String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getRole().name());

            return preparedStatement;
        }, keyHolder);

        final long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return User.withId(
                UserId.from(generatedId),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }
}
