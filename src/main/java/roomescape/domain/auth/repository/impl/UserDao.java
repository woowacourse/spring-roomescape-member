package roomescape.domain.auth.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;

@Repository
public class UserDao implements UserRepository {

    private static final String TABLE_NAME = "users";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public UserDao(final NamedParameterJdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public User save(final User user) {
        if (user.existId()) {
            return update(user);
        }

        return create(user);
    }

    @Override
    public boolean existsByName(final String name) {
        final String sql = "select exists(select 1 from " + TABLE_NAME + " where name = :name)";
        final Map<String, Object> params = Map.of("name", name);

        return jdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    @Override
    public boolean existsByEmail(final String email) {
        final String sql = "select exists(select 1 from " + TABLE_NAME + " where email = :email)";
        final Map<String, Object> params = Map.of("email", email);

        return jdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    @Override
    public Optional<User> findByUserId(final Long userId) {
        final String sql = "select id, name, email, password from " + TABLE_NAME + " where id = :id";
        final Map<String, Object> params = Map.of("id", userId);

        try {
            final User user = jdbcTemplate.queryForObject(sql, params, (resultSet, rowNum) -> userOf(resultSet));
            return Optional.ofNullable(user);
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        final String sql = "select id, name, email, password from " + TABLE_NAME + " where email = :email";
        final Map<String, Object> params = Map.of("email", email);

        try {
            final User user = jdbcTemplate.queryForObject(sql, params, (resultSet, rowNum) -> userOf(resultSet));
            return Optional.ofNullable(user);
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private User userOf(final ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .username(new Name(resultSet.getString("name")))
                .password(resultSet.getString("password"))
                .build();
    }

    private User update(final User user) {
        final String updateSql = String.format("""
                update %s set name=:name, email=:email, password=:password where id=:id
                """, TABLE_NAME);

        final MapSqlParameterSource params = new MapSqlParameterSource().addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("id", user.getId());

        final int updatedRowCount = jdbcTemplate.update(updateSql, params);

        if (updatedRowCount == 0) {
            throw new EntityNotFoundException("User not found id = " + user.getId());
        }

        return user;
    }

    private User create(final User user) {
        final MapSqlParameterSource params = new MapSqlParameterSource().addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword());

        final long id = jdbcInsert.executeAndReturnKey(params)
                .longValue();

        return new User(id, user.getUsername(), user.getEmail(), user.getPassword());
    }
}
