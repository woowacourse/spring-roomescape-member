package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeStatus;

@Repository
@RequiredArgsConstructor
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> rowMapper = (rs, rowNum) -> {

        Theme theme = Theme.create(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("thumbnail_url"),
                rs.getString("description")
        );

        if (rs.getString("status").equals(ThemeStatus.DELETED.toString())) {
            return theme.delete();
        }

        return theme;
    };

    public Theme save(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.name())
                .addValue("thumbnail_url", theme.thumbnailUrl())
                .addValue("description", theme.description())
                .addValue("status", ThemeStatus.AVAILABLE.toString());

        SimpleJdbcInsert themeInsertExecutor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");

        Number themeId = themeInsertExecutor.executeAndReturnKey(params);

        return Theme.create(
                themeId.longValue(),
                theme.name(),
                theme.thumbnailUrl(),
                theme.description()
        );
    }

    public void delete(long themeId) {
        String sql = "UPDATE theme SET status = 'DELETED' WHERE id = ?";
        int affected = jdbcTemplate.update(sql, themeId);

        if(affected == 0) {
            throw new NoSuchElementException("[ERROR] 삭제할 id에 해당하는 예약이 존재하지 않습니다.");
        }
    }

    public List<Theme> findAllThemes() {
        String sql = """
                SELECT * FROM theme
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Theme> findSortedPopularThemesBy(LocalDate startAt, LocalDate endAt, int limit) {
        String sql = """
                SELECT 
                    theme.id, 
                    theme.name, 
                    theme.thumbnail_url, 
                    theme.description,
                    theme.status,
                    COUNT(reservation.id) as count
                FROM reservation
                INNER JOIN theme ON reservation.theme_id = theme.id
                WHERE (reservation.date BETWEEN ? AND ?)
                    AND theme.status = ?
                GROUP BY 
                    theme.id,
                    theme.name,
                    theme.thumbnail_url,
                    theme.description,
                    theme.status
                ORDER BY COUNT(reservation.id) DESC
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, rowMapper, startAt, endAt, ThemeStatus.AVAILABLE.toString(), limit);
    }
}
