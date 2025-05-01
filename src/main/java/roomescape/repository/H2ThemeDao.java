package roomescape.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Theme;

@Repository
public class H2ThemeDao implements ThemeDao {

    private static final RowMapper<Theme> ROW_MAPPER = (rs, rowNum) -> new Theme(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("thumbnail")
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public H2ThemeDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Theme save(Theme theme) {
        String sql = "INSERT INTO theme (name, description, thumbnail) VALUES (:name, :description, :thumbnail)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        jdbcTemplate.update(sql, mapSqlParameterSource, keyHolder);

        Number key = keyHolder.getKey();
        return new Theme(key.longValue(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = :id";
        jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
    }

    @Override
    public List<Theme> sortByRank() {
        String sql = """
                SELECT
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description,
                    t.thumbnail
                FROM
                    reservation r
                JOIN
                    theme t ON r.theme_id = t.id
                WHERE
                    PARSEDATETIME(r.date, 'yyyy-MM-dd') BETWEEN CURRENT_DATE - 7 AND CURRENT_DATE - 1
                GROUP BY
                    t.id, t.name, t.description, t.thumbnail
                ORDER BY
                    COUNT(r.id) DESC
                LIMIT 10;
                """;

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public boolean isExistByName(String name) {
        String sql = "SELECT EXISTS (SELECT 1 FROM theme WHERE name = :name)";

        return Boolean.TRUE == jdbcTemplate.queryForObject(
                sql, new MapSqlParameterSource("name", name), Boolean.class);

    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "SELECT * FROM theme WHERE id = :id";

        List<Theme> findTheme = jdbcTemplate.query(
                sql, new MapSqlParameterSource("id", id),
                ROW_MAPPER);
        return findTheme.stream().findFirst();
    }
}
