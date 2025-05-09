package roomescape.login.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean existsByEmailAndPassword(final String email, final String password) {
        final String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM members AS m
                    WHERE m.email = ? AND m.password = ?
                    LIMIT 1
                )
                """;

        return jdbcTemplate.queryForObject(sql, Boolean.class, email, password);
    }

    @Override
    public Optional<String> findNameByEmail(final String email) {
        final String sql = "SELECT name FROM members WHERE email = ?";

        try {
            final String name = jdbcTemplate.queryForObject(sql, String.class, email);
            return Optional.ofNullable(name);
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
