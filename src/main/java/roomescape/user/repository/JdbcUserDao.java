package roomescape.user.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.user.domain.User;

@Repository
public class JdbcUserDao implements UserRepository {

    private final RowMapper<User> rowMapper = (rs, rowNum) ->
            new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
            );
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, name, email, password FROM member where email = ?";

        List<User> findUsers = jdbcTemplate.query(sql, rowMapper, email);

        if (findUsers.isEmpty()) {
            return Optional.empty();
        }
        if (findUsers.size() > 1) {
            throw new IllegalStateException("조회 결과가 2개 이상입니다.");
        }

        return Optional.of(findUsers.getFirst());
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT id, name, email, password FROM member where id = ?";
        List<User> findUsers = jdbcTemplate.query(sql, rowMapper, id);

        if (findUsers.isEmpty()) {
            return Optional.empty();
        }
        if (findUsers.size() > 1) {
            throw new IllegalStateException("조회 결과가 2개 이상입니다.");
        }

        return Optional.of(findUsers.getFirst());
    }
}
