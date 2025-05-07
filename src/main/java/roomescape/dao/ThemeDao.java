package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;

@Repository
public class ThemeDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ThemeDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Theme save(Theme theme) {
        String sql = """
                INSERT INTO theme(name, description, thumbnail) VALUES(:name, :description, :thumbnail)
                """;
        Map<String, Object> params = Map.of(
                "name", theme.getName(),
                "description", theme.getDescription(),
                "thumbnail", theme.getThumbnail()
        );
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                sql,
                new MapSqlParameterSource(params),
                keyHolder,
                new String[]{"id"}
        );

        Long id = keyHolder.getKey().longValue();

        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public boolean deleteById(Long id) {
        String sql = """
                DELETE FROM theme WHERE id = :id
                """;
        int deletedRow = namedParameterJdbcTemplate.update(
                sql,
                Map.of("id", id)
        );
        return deletedRow == 1;
    }

    public List<Theme> findAll() {
        String sql = """
                SELECT id, name, description, thumbnail FROM THEME
                """;

        return namedParameterJdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                )
        );
    }

    public Optional<Theme> findById(Long id) {
        try {
            String sql = """
                    SELECT id, name, description, thumbnail
                    FROM THEME
                    WHERE id = :id
                    """;
            Theme theme = namedParameterJdbcTemplate.queryForObject(
                    sql,
                    Map.of("id", id),
                    (rs, rowNum) -> new Theme(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("thumbnail")
                    )
            );
            return Optional.ofNullable(theme);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Theme> getPopularThemeByRankAndDuration(int rank, LocalDate startAt, LocalDate endAt) {
        String sql = """
                SELECT id, name, description, thumbnail FROM THEME AS t
                INNER JOIN
                (SELECT THEME_ID, count(THEME_ID) AS COUNT
                FROM RESERVATION
                WHERE date BETWEEN :startAt AND :endAt
                GROUP BY THEME_ID
                ORDER BY COUNT DESC, THEME_ID ASC
                LIMIT :rank) AS popular
                ON t.ID = popular.THEME_ID
                """;

        Map<String, Object> params = Map.of(
                "startAt", startAt,
                "endAt", endAt,
                "rank", rank
        );

        return namedParameterJdbcTemplate.query(
                sql,
                new MapSqlParameterSource(params),
                (rs, rowNum) -> new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                )
        );
    }

    public boolean isExistThemeName(String name) {
        String sql = """
                SELECT EXISTS (SELECT 1 FROM theme WHERE name = :name)
                """;
        return namedParameterJdbcTemplate.queryForObject(
                sql,
                Map.of("name", name),
                Boolean.class
                );
    }
}
