package roomescape.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Theme;

import java.util.List;
import java.util.Optional;

@Repository
public class ThemeDaoImpl implements ThemeDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ThemeDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
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

        return jdbcTemplate.query(sql, getThemeRowMapper());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = :id";
        jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "SELECT * FROM theme WHERE id = :id";

        List<Theme> findTheme = jdbcTemplate.query(
            sql, new MapSqlParameterSource("id", id),
            getThemeRowMapper());
        return findTheme.stream().findFirst();
    }

    private RowMapper<Theme> getThemeRowMapper() {
        return (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
        );
    }
}
