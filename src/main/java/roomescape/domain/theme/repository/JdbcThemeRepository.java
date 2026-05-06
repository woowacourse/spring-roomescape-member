package roomescape.domain.theme.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.entity.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private static final String FIND_ALL_THEMES_QUERY = """
            SELECT * FROM theme;
            """;

    private static final String FIND_THEME_BY_ID_QUERY = """
            SELECT * FROM theme
            WHERE id = :id;
            """;

    private static final String DELETE_THEME_BY_ID_QUERY = """
            DELETE FROM theme
            WHERE id = :id;
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcThemeRepository(
            NamedParameterJdbcTemplate jdbcTemplate,
            DataSource dataSource
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_THEMES_QUERY,
                themeRowMapper()
        );
    }

    @Override
    public Optional<Theme> findById(Long id) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("id", "id");

            Theme theme = jdbcTemplate.queryForObject(
                    FIND_THEME_BY_ID_QUERY,
                    parameters,
                    themeRowMapper()
            );

            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Theme save(Theme theme) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail_url", theme.getThumbnailUrl());

        Number key = simpleJdbcInsert.executeAndReturnKey(parameters);
        long generatedId = key.longValue();

        theme.setId(generatedId);

        return theme;
    }

    @Override
    public void deleteById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        jdbcTemplate.update(
                DELETE_THEME_BY_ID_QUERY,
                parameters
        );
    }

    private RowMapper<Theme> themeRowMapper() {
        return (resultSet, rowNumber) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("")
        );
    }
}
