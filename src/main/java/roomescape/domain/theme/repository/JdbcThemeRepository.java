package roomescape.domain.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.entity.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcThemeRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("theme")
            .usingColumns("name", "description", "image_url")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAllThemes() {
        String sql = "SELECT id, name, description, image_url FROM themes";
        return jdbcTemplate.query(
            sql,
            (resultSet, rowNum) -> Theme.reconstruct(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("image_url")
            ));
    }

    @Override
    public Theme save(Theme theme) {
        Map<String, Object> args = Map.of(
            "name", theme.getName(),
            "description", theme.getDescription(),
            "image_url", theme.getImageUrl()
        );
        long generatedKey = simpleJdbcInsert.executeAndReturnKey(args).longValue();
        return Theme.reconstruct(generatedKey, theme.getName(), theme.getDescription(), theme.getImageUrl());
    }

    @Override
    public void deleteThemeById(Long id) {
        final String sql = "DELETE FROM theme WHERE id = :id";
        final SqlParameterSource parameters = new MapSqlParameterSource("id", id);

        jdbcTemplate.update(sql, parameters);
    }

    @Override
    public Optional<Theme> findThemeById(Long id) {
        final String sql = "SELECT * FROM theme WHERE id = :id";
        final SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        try {
            Theme theme = jdbcTemplate.queryForObject(
                sql,
                parameters,
                (resultSet, rowNum) -> Theme.reconstruct(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("image_url")
                )
            );
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Theme> findPopularThemesDateBetween(LocalDate startDate, LocalDate endDate, Integer limit) {
        String sql = """
            SELECT t.id, t.name, t.description, t.image_url
            FROM theme t
            JOIN reservation r ON t.id = r.theme_id
            WHERE r.date BETWEEN :startDate AND :endDate
            GROUP BY t.id, t.name, t.description, t.image_url
            ORDER BY COUNT(r.id) DESC, t.id ASC
            LIMIT :limit
            """;

        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(
            "startDate", startDate,
            "endDate", endDate,
            "limit", limit
        ));
        return jdbcTemplate.query(
            sql,
            parameters,
            (resultSet, rowNum) -> Theme.reconstruct(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("image_url")
            ));
    }
}
