package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> themeRowMapper;

    public ThemeDao(final JdbcTemplate jdbcTemplate, final RowMapper<Theme> themeRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.themeRowMapper = themeRowMapper;
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
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    public Optional<Theme> findById(final long themeId) {
        String sql = "SELECT * FROM theme WHERE id = ? ";
        return jdbcTemplate.query(sql, themeRowMapper, themeId)
                .stream()
                .findAny();
    }

    public void delete(final long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, Long.valueOf(id));
    }
}
