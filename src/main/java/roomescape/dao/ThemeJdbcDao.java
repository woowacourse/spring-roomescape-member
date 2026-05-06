package roomescape.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.domain.vo.Name;
import roomescape.dto.response.AvailableTimeResponseDto;


@Repository
public class ThemeJdbcDao implements ThemeDao {
    public static final RowMapper<Theme> THEME_ROW_MAPPER = (rs, rowNum) ->
            new Theme(
                    rs.getLong("id"),
                    new Name(rs.getString("name")),
                    rs.getString("thumbnail_url"),
                    rs.getString("description")
            );

    private static final RowMapper<AvailableTimeResponseDto> AVAILABLE_TIME_MAPPER = (rs, rowNum) ->
        new AvailableTimeResponseDto(
                rs.getLong("time_id"),
                LocalTime.parse(rs.getString("start_at")),
                rs.getBoolean("already_booked")
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

        return jdbcTemplate.query(sql, THEME_ROW_MAPPER);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = """
                SELECT * FROM themes
                WHERE id = :id
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(sql, params, THEME_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Theme insert(Theme theme) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("themes")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName().getValue())
                .addValue("thumbnailUrl", theme.getThumbnailUrl())
                .addValue("description", theme.getDescription());

        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Theme(id, theme.getName(), theme.getThumbnailUrl(), theme.getDescription());
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
    public boolean existsByName(Name name) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1 FROM themes WHERE name = :name
                )
                """;
        SqlParameterSource params = new MapSqlParameterSource("name", name.getValue());

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }

    @Override
    public List<AvailableTimeResponseDto> findAvailableTimesById(Long themeId, LocalDate localDate) {
        String sql = """
                SELECT
                    t.id as time_id,
                    t.start_at as start_at, 
                     EXISTS(
                         SELECT 1 FROM reservations r
                         WHERE r.time_id = t.id
                         AND r.theme_id = :theme_id
                         AND r.date = :date
                     ) as already_booked
                 FROM times t
                 ORDER BY t.start_at
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("theme_id", themeId)
                .addValue("date", localDate);

        return jdbcTemplate.query(sql, params, AVAILABLE_TIME_MAPPER);
    }
}
