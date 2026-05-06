package roomescape.theme.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository{
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Theme> themeRowMapper = (resultSet, rowMapper) ->
            Theme.load(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail_url"),
                    resultSet.getBoolean("is_active")
            );

    public JdbcThemeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), themeRowMapper);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "SELECT * FROM theme WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, themeRowMapper));
    }

    @Override
    public List<Theme> findByStatus(boolean status) {
        String sql = "SELECT * FROM theme WHERE is_active = :status ORDER BY name ASC";
        SqlParameterSource params = new MapSqlParameterSource("status", status);
        return jdbcTemplate.query(sql, params, themeRowMapper);
    }

    @Override
    public Theme save(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.name())
                .addValue("description", theme.description())
                .addValue("thumbnail_url", theme.thumbnailUrl())
                .addValue("is_active", theme.isActive());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Theme.load(id, theme.name(), theme.description(), theme.thumbnailUrl(), theme.isActive());
    }

    @Override
    public boolean updateStatus(Theme theme) {
        String sql = """
                    UPDATE theme 
                    SET name = :name, description = :description,
                        thumbnail_url = :thumbnail_url, is_active = :is_active
                    WHERE id = :id
                    """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", theme.id())
                .addValue("name", theme.name())
                .addValue("description", theme.description())
                .addValue("thumbnail_url", theme.thumbnailUrl())
                .addValue("is_active", theme.isActive());

        int updateCount = jdbcTemplate.update(sql, params);
        return updateCount > 0;
    }

    //TODO: findPoupular ? 인기 테마 조회 추가
}
