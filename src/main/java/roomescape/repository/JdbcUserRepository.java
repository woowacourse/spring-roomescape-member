package roomescape.repository;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.domain.MemberName;
import roomescape.domain.UserRepository;

@Repository
public class JdbcUserRepository implements UserRepository {

    private static final RowMapper<Member> USER_MAPPER = (resultSet, row) ->
            new Member(
                    resultSet.getLong("id"),
                    new MemberName(resultSet.getString("name")),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    Role.of(resultSet.getString("role"))
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, USER_MAPPER, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, USER_MAPPER, email));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
