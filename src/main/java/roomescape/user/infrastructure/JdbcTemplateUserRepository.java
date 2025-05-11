package roomescape.user.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.domain.Email;
import roomescape.common.jdbc.JdbcUtils;
import roomescape.user.domain.Password;
import roomescape.user.domain.User;
import roomescape.user.domain.UserId;
import roomescape.user.domain.UserName;
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
                    UserName.from(resultSet.getString("name")),
                    Email.from(resultSet.getString("email")),
                    Password.fromEncoded(resultSet.getString("password")),
                    UserRole.valueOf(resultSet.getString("role"))
            );

    @Override
    public Optional<User> findById(final UserId id) {
        final String sql = """
                SELECT id, name, email, password, role
                FROM users
                WHERE email = ?
                """;
        return JdbcUtils.queryForOptional(jdbcTemplate, sql, userRowMapper, id.getValue());
    }

    @Override
    public Optional<User> findByEmail(final Email email) {
        final String sql = """
                SELECT id, name, email, password, role
                FROM users
                WHERE email = ?
                """;
        return JdbcUtils.queryForOptional(jdbcTemplate, sql, userRowMapper, email.getValue());
    }

    @Override
    public User save(final User user) {
        final String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName().getValue());
            preparedStatement.setString(2, user.getEmail().getValue());
            preparedStatement.setString(3, user.getPassword().getEncodedValue());
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
