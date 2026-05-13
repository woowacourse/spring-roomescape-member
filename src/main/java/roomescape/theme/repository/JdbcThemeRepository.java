package roomescape.theme.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;
import roomescape.error.ErrorCode;
import roomescape.theme.exception.ThemeException;
import roomescape.theme.service.dto.ThemeBestServiceDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert themeInsert;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.themeInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query(
                """
                        SELECT t.id, t.name, t.description, t.image_url
                        FROM theme t
                        """,
                new JdbcThemeRepository.ThemeRowMapper()
        );
    }

    @Override
    public Theme findById(Long id) {
        List<Theme> themes = jdbcTemplate.query(
                """
                        SELECT t.id, t.name, t.description, t.image_url
                        FROM theme t
                        WHERE t.id = ?
                        """,
                new JdbcThemeRepository.ThemeRowMapper(),
                id
        );
        if (themes.isEmpty()) {
            throw new ThemeException(ErrorCode.THEME_NOT_FOUND);
        }
        return themes.get(0);
    }

    @Override
    public Theme save(Theme theme) {
        Number id = themeInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("image_url", theme.getImageUrl()));
        return theme.withId(id.longValue());
    }

    @Override
    public boolean existsById(Long id) {
        Integer exists = jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM theme WHERE id = ?)",
                Integer.class,
                id
        );
        return exists != null && exists == 1;
    }

    @Override
    public boolean deleteById(Long id) {
        int affectedRows = jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
        return affectedRows > 0;
    }

    @Override
    public List<Theme> findBestThemesByDate(ThemeBestServiceDto themeBestServiceDto) {
        LocalDate date = themeBestServiceDto.date();
        LocalDate startDate = date.minusDays(themeBestServiceDto.dayCount());
        LocalDate endDate = date.minusDays(1);
        return jdbcTemplate.query(
                """
                        SELECT t.id, t.name, t.description, t.image_url
                        FROM theme t
                        JOIN reservation r ON r.theme_id = t.id
                        WHERE r.date BETWEEN ? AND ?
                        GROUP BY t.id, t.name, t.description, t.image_url
                        ORDER BY COUNT(r.id) DESC, t.id ASC
                        LIMIT ?
                        """,
                new ThemeRowMapper(),
                startDate,
                endDate,
                themeBestServiceDto.rankCount()
        );
    }

    private static class ThemeRowMapper implements RowMapper<Theme> {

        @Override
        public Theme mapRow(ResultSet rs, int rowNum) throws SQLException {
            Theme theme = new Theme(
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("image_url")
            );
            return theme.withId(rs.getLong("id"));
        }
    }
}
