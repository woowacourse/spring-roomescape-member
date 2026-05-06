package roomescape.dao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.domain.vo.Name;

@Repository
public class ThemeJdbcDao implements ThemeDao {
    public static final RowMapper<Theme> ROW_MAPPER = (rs, rowNum) ->
            new Theme(
                    rs.getLong("id"),
                    new Name(rs.getString("name")),
                    rs.getString("thumbnail_url"),
                    rs.getString("description")
            );
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ThemeJdbcDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Theme> findAll() {
        String sql = """
                SELECT * FROM themes
                """;

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = """
                SELECT * FROM themes
                WHERE id = :id
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(sql, params, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Theme insert(Theme theme) {
        String sql = """
                INSERT INTO themes(name, thumbnail_url, description)
                VALUES(:name, :thumbnailUrl, :description)
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName().getValue())
                .addValue("thumbnailUrl", theme.getThumbnailUrl())
                .addValue("description", theme.getDescription());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return new Theme(
                id, theme.getName(), theme.getThumbnailUrl(), theme.getDescription()
        );
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
}
