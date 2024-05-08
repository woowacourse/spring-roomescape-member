package roomescape.repository;

import java.sql.PreparedStatement;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.User;

@Repository
public class JdbcUserRepository implements UserRepository {

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password")
    );
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Optional<User> findById(long id) {
        try {
            User findUser = jdbcTemplate.queryForObject(
                    "SELECT id, name, email, password FROM MEMBER WHERE id = ?",
                    USER_ROW_MAPPER,
                    id
            );
            return Optional.of(findUser);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            User findUser = jdbcTemplate.queryForObject(
                    "SELECT id, name, email, password FROM MEMBER WHERE email = ?",
                    USER_ROW_MAPPER,
                    email
            );
            return Optional.of(findUser);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO MEMBER (NAME, EMAIL, PASSWORD) VALUES (?,?,?)",
                    new String[]{"id"}
            );
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            return preparedStatement;
        }), keyHolder);

        long savedId = keyHolder.getKey().longValue();
        return findById(savedId).get();
    }
}
