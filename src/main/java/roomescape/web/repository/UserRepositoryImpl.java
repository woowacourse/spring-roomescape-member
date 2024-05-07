package roomescape.web.repository;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.core.domain.User;
import roomescape.core.repository.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public UserRepositoryImpl(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("user")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public User findByEmailAndPassword(final String email) {
        return new User(1L, "brown", "test@email.com", "password");
    }

    @Override
    public User findByEmail(final String email) {
        return new User(1L, "brown", "test@email.com", "password");
    }
}
