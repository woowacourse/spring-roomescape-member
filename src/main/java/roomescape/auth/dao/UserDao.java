package roomescape.auth.dao;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.auth.domain.Users;
import roomescape.exception.RoomEscapeException;
import roomescape.exception.message.ExceptionMessage;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public UserDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Users> usersRowMapper = ((resultSet, rowNum) -> new Users(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("password"))
    );

    public Users getByEmailAndPassword(final String email, final String password) {
        final String sql = "select * from users where email = ? and password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, usersRowMapper, email, password);
        } catch (final EmptyResultDataAccessException exception) {
            throw new RoomEscapeException(ExceptionMessage.INVALID_USER_NAME);
        }
    }


}
