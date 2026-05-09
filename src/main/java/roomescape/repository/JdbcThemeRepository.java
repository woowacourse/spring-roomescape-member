package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
    }

    private static RowMapper<Theme> getThemeRowMapper() {
        return (resultSet, rowNum) -> Theme.of(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("image_url")
        );
    }

    @Override
    public Theme save(Theme theme) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");

        long generatedKey = insert.executeAndReturnKey(
                new BeanPropertySqlParameterSource(theme)
        ).longValue();

        return Theme.of(
                generatedKey,
                theme.getName(),
                theme.getDescription(),
                theme.getImageUrl()
        );
    }

    @Override
    public List<Theme> findAll() {
        String sql = "select id, name, description, image_url from theme";

        return jdbcTemplate.query(
                sql,
                getThemeRowMapper()
        );
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "select id, name, description, image_url from theme where id = :id";

        Map<String, Object> params = Map.of("id", id);

        List<Theme> themes = jdbcTemplate.query(
                sql,
                params,
                getThemeRowMapper()
        );
        return themes.stream()
                .findFirst();
    }

    @Override
    public List<Long> findPopularThemeIds() {
        List<Long> popularThemeIds = new LinkedList<>();
        String sql = """
                    SELECT t.id AS theme_id, t.name, COUNT(r.id) AS reservation_count
                    FROM theme AS t
                    LEFT JOIN reservation AS r
                    ON r.theme_id = t.id
                    AND r.date >= :aWeekAgo
                    AND r.date < :today
                    GROUP BY t.id
                    ORDER BY reservation_count DESC, t.name ASC
                    LIMIT 10
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("aWeekAgo", LocalDateTime.now().minusWeeks(1))
                .addValue("today", LocalDate.now());

        jdbcTemplate.query(
                sql,
                params,
                (resultSet, rowNum) -> popularThemeIds.add(resultSet.getLong("theme_id"))
        );
        return popularThemeIds;
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from theme where id = :id";
        Map<String, Object> params = Map.of("id", id);
        jdbcTemplate.update(sql, params);
    }
}


