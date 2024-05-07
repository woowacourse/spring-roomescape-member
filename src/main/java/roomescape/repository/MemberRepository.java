package roomescape.repository;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.User;

@Repository
public class MemberRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("user_table")
                .usingGeneratedKeyColumns("id");
    }

    public User save(User user) {
        Long userId = jdbcInsert.executeAndReturnKey(Map.of(
                        "name", user.getName(),
                        "email", user.getEmail(),
                        "password", user.getPassword()))
                .longValue();

        return new User(
                userId,
                user.getName(),
                user.getEmail(),
                user.getPassword());
    }
}
