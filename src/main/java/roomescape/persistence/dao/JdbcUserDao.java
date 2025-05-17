package roomescape.persistence.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.User;
import roomescape.persistence.entity.UserEntity;

@Repository
public class JdbcUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> find(final Long id) {
        final String sql = "SELECT id, name, email, password, role FROM users WHERE id = ?";
        try {
            final UserEntity userEntity = jdbcTemplate.queryForObject(sql, UserEntity.getDefaultRowMapper(), id);
            return Optional.of(userEntity.toDomain());
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        final String sql = "SELECT id, name, email, password, role FROM users";

        return jdbcTemplate.query(sql, UserEntity.getDefaultRowMapper()).stream()
                .map(UserEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findByPrincipal(final String principal) {
        final String sql = "SELECT id, name, email, password, role FROM users WHERE email = ?";
        try {
            final UserEntity userEntity = jdbcTemplate.queryForObject(
                    sql,
                    UserEntity.getDefaultRowMapper(),
                    principal
            );
            return Optional.of(userEntity.toDomain());
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
