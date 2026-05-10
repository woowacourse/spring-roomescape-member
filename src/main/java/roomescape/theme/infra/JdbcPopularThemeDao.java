package roomescape.theme.infra;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.application.query.PopularTheme;
import roomescape.theme.application.query.PopularThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.repository.ThemeRepository;

@RequiredArgsConstructor
@Repository
public class JdbcPopularThemeDao implements PopularThemeDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<PopularTheme> findTop10PopularThemesBetween(LocalDate from, LocalDate to) {
        String sql = """
                SELECT t.id, t.name, t.description, t.thumbnail_img_url, COUNT(*) as reserved_count
                FROM theme t
                JOIN reservation r ON t.id = r.theme_id
                WHERE r.date BETWEEN ? AND ?
                GROUP BY t.id, t.name, t.description, t.thumbnail_img_url
                ORDER BY reserved_count DESC
                LIMIT 10
                """;

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new PopularTheme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail_img_url"),
                        rs.getInt("reserved_count")
                ), from, to);
    }
}
