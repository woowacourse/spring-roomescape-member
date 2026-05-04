package roomescape.theme.repository;

import java.sql.PreparedStatement;
import java.time.Duration;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.entity.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    // TODO: 요구사항에 따라 theme의 runtime은 고정한다.
    private static final Long RUNTIME = 1L;
    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Theme save(String name, String description, String thumbnailUrl) {
        String sql = """
                INSERT INTO theme (name, description, thumbnail_url, runtime)
                VALUES (?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, thumbnailUrl);
            ps.setLong(4, RUNTIME);
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return Theme.of(
                id,
                name,
                description,
                thumbnailUrl,
                Duration.ofHours(RUNTIME)
        );
    }
}