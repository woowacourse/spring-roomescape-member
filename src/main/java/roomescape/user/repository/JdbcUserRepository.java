package roomescape.user.repository;

import java.sql.PreparedStatement;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.user.domain.User;
import roomescape.user.exception.NotFoundUserException;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password")
    );

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(User user) {
        Long id = insertWithKeyHolder(user);
        return findByIdOrThrow(id);
    }

    @Override
    public User findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundUserException("해당 유저 id가 존재하지 않습니다."));
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "select id, name, email, password from users where id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existUserByEmailAndPassword(String email, String password) {
        String sql = "SELECT EXISTS(SELECT 1 FROM users WHERE email = ? AND password = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, email, password);
    }

    @Override
    public Optional<User> findUseByEmail(String email) {
        String sql = "select id, name, email, password from users where email = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, name, email, password "
                + "FROM users "
                + "WHERE email = ? AND password = ?";

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, userRowMapper, email, password));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Long insertWithKeyHolder(User user) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
