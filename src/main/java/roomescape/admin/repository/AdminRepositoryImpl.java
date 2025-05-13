package roomescape.admin.repository;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.admin.domain.Admin;

@Repository
public class AdminRepositoryImpl implements AdminRepository {

    private final JdbcTemplate jdbcTemplate;

    public AdminRepositoryImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Admin> findByLoginId(String loginId) {
        String sql = "SELECT * FROM admin WHERE login_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                sql,
                (resultSet, rowNum) -> new Admin(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("login_id"),
                    resultSet.getString("password")
                ),
                loginId
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByLoginIdAndPassword(String loginId, String password) {
        String sql = "SELECT COUNT(*) FROM admin WHERE login_id = ? AND password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, loginId, password) > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
