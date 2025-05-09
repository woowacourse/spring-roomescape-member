package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.model.user.User;
import roomescape.model.user.UserEmail;
import roomescape.model.user.UserName;
import roomescape.model.user.UserPassword;

@Repository
public class JdbcLoginRepository implements LoginRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcLoginRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) ->
            new User(
                    rs.getLong("id"),
                    new UserName(rs.getString("name")),
                    new UserEmail(rs.getString("email")),
                    new UserPassword(rs.getString("password")
                    )
            );

    @Override
    public User login(String email, String password) {
        String sql = "select id, name, email, password from member where email = ? and password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, email, password);
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: " + email, ex);
        }
    }

    @Override
    public UserName findUserNameByUserEmail(String userEmail) {
        String sql = "select name from member where email = ?";
        try {
            return new UserName(jdbcTemplate.queryForObject(sql, String.class, userEmail));
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: userEmail :" + userEmail, ex);
        }
    }
}
