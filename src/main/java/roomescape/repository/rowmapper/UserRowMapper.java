package roomescape.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.user.User;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(final ResultSet resultSet, final int rowNumber) {
        try {
            return User.of(
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
