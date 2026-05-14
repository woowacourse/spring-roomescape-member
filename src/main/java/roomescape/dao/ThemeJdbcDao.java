package roomescape.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.row.AvailableTimeRow;
import roomescape.dao.row.ThemeRow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Repository
public class ThemeJdbcDao implements ThemeDao {
    private static final RowMapper<ThemeRow> THEME_ROW_MAPPER = (rs, rowNum) ->
            new ThemeRow(
                    rs.getLong("theme_id"),
                    rs.getString("theme_name"),
                    rs.getString("theme_thumbnail_url"),
                    rs.getString("theme_description")
            );


    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert themeInsert;

    public ThemeJdbcDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.themeInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("themes")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "thumbnail_url", "description");
    }

    @Override
    public List<ThemeRow> findAll() {
        String sql = """
                SELECT
                    id AS theme_id,
                    name AS theme_name,
                    thumbnail_url AS theme_thumbnail_url,
                    description AS theme_description
                FROM themes
                """;

        return jdbcTemplate.query(sql, THEME_ROW_MAPPER);
    }

    @Override
    public Optional<ThemeRow> findById(Long id) {
        String sql = """
                SELECT
                     id AS theme_id,
                     name AS theme_name,
                     thumbnail_url AS theme_thumbnail_url,
                     description AS theme_description
                 FROM themes
                 WHERE id = :id
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(sql, params, THEME_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public ThemeRow create(ThemeRow theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.name())
                .addValue("thumbnail_url", theme.thumbnailUrl())
                .addValue("description", theme.description());

        Long id = themeInsert.executeAndReturnKey(params).longValue();

        return new ThemeRow(id, theme.name(), theme.thumbnailUrl(), theme.description());
    }

    @Override
    public int delete(Long id) {
        String sql = """
                DELETE FROM themes
                WHERE id = :id
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.update(sql, params);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1 FROM themes th
                    WHERE th.id = :id
                )
                """;
        SqlParameterSource params = new MapSqlParameterSource("id", id);

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }

    @Override
    public boolean existsByName(String name) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1 FROM themes WHERE name = :name
                )
                """;
        SqlParameterSource params = new MapSqlParameterSource("name", name);

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }
}
