package roomescape.theme.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.entity.ThemeEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcThemeRepository implements ThemeRepository {
    private final RowMapper<ThemeEntity> ROW_MAPPER = (resultSet, rowNum) -> {
        final long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String thumbnail = resultSet.getString("thumbnail");
        return new ThemeEntity(
                id,
                name,
                description,
                thumbnail
        );
    };
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public ThemeEntity save(ThemeEntity entity) {
        String query = "INSERT INTO theme (name, description, thumbnail) VALUES (:name, :description, :thumbnail)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", entity.getName())
                .addValue("description", entity.getDescription())
                .addValue("thumbnail", entity.getThumbnail());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, params, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new ThemeEntity(
                id,
                entity.getName(),
                entity.getDescription(),
                entity.getThumbnail()
        );
    }

    @Override
    public List<ThemeEntity> findAll() {
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
    public Optional<ThemeEntity> findById(Long id) {
        String query = "SELECT * FROM theme WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            ThemeEntity themeEntity = jdbcTemplate.queryForObject(query, params, ROW_MAPPER);
            return Optional.of(themeEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ThemeEntity> findByName(String name) {
        String query = "SELECT * FROM theme WHERE name = :name";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", name);
        try {
            ThemeEntity themeEntity = jdbcTemplate.queryForObject(query, param, ROW_MAPPER);
            return Optional.of(themeEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ThemeEntity> findPopularThemesByDateRangeAndLimit(
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
                FETCH FIRST :limit ROWS ONLY
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("limit", limit);
        return jdbcTemplate.query(query, params, ROW_MAPPER);
    }
}
