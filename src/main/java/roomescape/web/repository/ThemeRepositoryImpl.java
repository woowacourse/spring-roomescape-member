package roomescape.web.repository;

import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.core.domain.Theme;
import roomescape.core.repository.ThemeRepository;

@Repository
public class ThemeRepositoryImpl implements ThemeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeRepositoryImpl(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(final Theme theme) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query("SELECT id, name, description, thumbnail FROM theme", getThemeRowMapper());
    }

    @Override
    public List<Theme> findPopular() {
        final LocalDate today = LocalDate.now();
        final LocalDate lastWeek = today.minusWeeks(1);

        final String query = """
                SELECT t.id, t.name, t.description, t.thumbnail
                FROM theme as t
                JOIN reservation as r ON t.id = r.theme_id
                WHERE r.date BETWEEN ? AND ?
                GROUP BY t.id
                ORDER BY count(r.id) DESC
                LIMIT 10
                """;
        
        return jdbcTemplate.query(query, getThemeRowMapper(), lastWeek, today);
    }

    @Override
    public Theme findById(final long id) {
        try {
            final String query = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
            return jdbcTemplate.queryForObject(query, getThemeRowMapper(), id);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Theme not found");
        }
    }

    private RowMapper<Theme> getThemeRowMapper() {
        return (resultSet, rowNum) -> {
            final Long timeId = resultSet.getLong("id");
            final String name = resultSet.getString("name");
            final String description = resultSet.getString("description");
            final String thumbnail = resultSet.getString("thumbnail");

            return new Theme(timeId, name, description, thumbnail);
        };
    }

    @Override
    public Integer countByName(final String name) {
        final String query = """
                SELECT count(*)
                FROM theme as t
                WHERE t.name = ?
                """;

        return jdbcTemplate.queryForObject(query, Integer.class, name);
    }

    @Override
    public Theme deleteById(final long id) {
        return null;
    }
}
