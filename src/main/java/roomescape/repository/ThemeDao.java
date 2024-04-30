package roomescape.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Theme save(final Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO theme(name, description, thumbnail) VALUES (?, ?, ?)";
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            sql,
                            new String[]{"id"}
                    );
                    ps.setString(1, theme.getName());
                    ps.setString(2, theme.getDescription());
                    ps.setString(3, theme.getThumbnail());
                    return ps;
                }, keyHolder
        );

        try {
            long id = keyHolder.getKey().longValue();
            return new Theme(
                    id,
                    theme.getName(),
                    theme.getDescription(),
                    theme.getThumbnail()
            );
        } catch (NullPointerException exception) {
            throw new RuntimeException("[ERROR] 테마 추가 요청이 정상적으로 이루어지지 않았습니다.");
        }
    }

    public List<Theme> getAll() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, (result, rowNum) ->
                new Theme(result.getLong("id"),
                        result.getString("name"),
                        result.getString("description"),
                        result.getString("thumbnail")
                )
        );
    }

    public Optional<Theme> findById(final long themeId) {
        String sql = "SELECT * FROM theme WHERE id = ? ";
        return jdbcTemplate.query(sql, (result, rowNum) ->
                        new Theme(result.getLong("id"),
                                result.getString("name"),
                                result.getString("description"),
                                result.getString("thumbnail")
                        ), themeId)
                .stream()
                .findAny();
    }
}
