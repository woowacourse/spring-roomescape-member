package roomescape.persistence;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.User;
import roomescape.domain.UserRepository;
import roomescape.persistence.query.CreateUserQuery;

import java.sql.PreparedStatement;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    public static final RowMapper<User> userRowMapper = (rs, rowNum) ->
            new User(rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByEmailAndPassword(final String email, final String password) {
        String sql = "SELECT id, name, email, password FROM users WHERE email = ? and password = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, email, password);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(final Long id) {
        String sql = "SELECT id, name, email, password FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, id);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Long create(final CreateUserQuery createUserQuery) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, createUserQuery.name());
            ps.setString(2, createUserQuery.email());
            ps.setString(3, createUserQuery.password());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
