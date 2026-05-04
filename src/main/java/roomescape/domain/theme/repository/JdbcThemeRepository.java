package roomescape.domain.theme.repository;

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
            .usingColumns("name", "description", "imageUrl")
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
}
