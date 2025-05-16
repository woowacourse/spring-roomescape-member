package roomescape.theme.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.entity.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcThemeRepository implements ThemeRepository {
    private final RowMapper<Theme> ROW_MAPPER = (resultSet, rowNum) ->
        new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public Theme save(Theme entity) {
        String query = "INSERT INTO theme (name, description, thumbnail) VALUES (:name, :description, :thumbnail)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", entity.getName())
                .addValue("description", entity.getDescription())
                .addValue("thumbnail", entity.getThumbnail());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, params, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new Theme(
                id,
                entity.getName(),
                entity.getDescription(),
                entity.getThumbnail()
        );
    }

    @Override
    public List<Theme> findAll() {
        String query = "SELECT * FROM theme";
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    @Override
    public boolean deleteById(Long id) {
        String query = "DELETE FROM theme WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        final int updated = jdbcTemplate.update(query, params);
        return updated > 0;
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String query = "SELECT * FROM theme WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            Theme theme = jdbcTemplate.queryForObject(query, params, ROW_MAPPER);
            return Optional.of(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    
    @Override
    public Optional<Theme> findByName(String name) {
        String query = "SELECT * FROM theme WHERE name = :name";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", name);
        try {
            Theme theme = jdbcTemplate.queryForObject(query, param, ROW_MAPPER);
            return Optional.of(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Theme> findPopularThemesByDateRangeAndLimit(
            LocalDate startDate,
            LocalDate endDate,
            final int limit
    ) {
        String query = """
                SELECT
                    t.id,
                    t.name,
                    t.description,
                    t.thumbnail,
                    r_stats.cnt
                FROM theme as t
                LEFT JOIN (
                    SELECT
                        theme_id,
                        COUNT(*) as cnt
                    FROM reservation
                    WHERE date BETWEEN :startDate AND :endDate
                    GROUP BY theme_id
                ) as r_stats
                ON t.id = r_stats.theme_id
                ORDER BY cnt DESC, theme_id DESC
                LIMIT :limit
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("limit", limit);
        return jdbcTemplate.query(query, params, ROW_MAPPER);
    }
}
