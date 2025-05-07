package roomescape.domain.auth.repository.impl;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.EntityNotFoundException;
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
    public boolean existsByName(final String name) {
        final String sql = "select exists(select 1 from " + TABLE_NAME + " where name = :name)";
        final Map<String, Object> params = Map.of("name", name);

        return jdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    @Override
    public User save(final User user) {
        if (user.existId()) {
            return update(user);
        }

        return create(user);
    }

    private User update(final User user) {
        final String updateSql = String.format("""
                update %s set name=:name, email=:email, password=:password where id=:id
                """, TABLE_NAME);

        final MapSqlParameterSource params = new MapSqlParameterSource().addValue("name", user.getUsername())
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
        final MapSqlParameterSource params = new MapSqlParameterSource().addValue("name", user.getUsername())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword());

        final long id = jdbcInsert.executeAndReturnKey(params)
                .longValue();

        return new User(id, user.getUsername(), user.getEmail(), user.getPassword());
    }

    @Override
    public boolean existsByEmail(final String email) {
        return false;
    }
}
