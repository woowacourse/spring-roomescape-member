package roomescape.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.row.AvailableTimeRow;
import roomescape.dao.row.ThemeRow;
import roomescape.dto.request.PopularThemeRequestDto;

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

    private static final RowMapper<AvailableTimeRow> AVAILABLE_TIME_MAPPER = (rs, rowNum) ->
            new AvailableTimeRow(
                    rs.getLong("time_id"),
                    LocalTime.parse(rs.getString("time_start_at")),
                    rs.getBoolean("already_booked")
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
    public boolean existsByName(String name) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1 FROM themes WHERE name = :name
                )
                """;
        SqlParameterSource params = new MapSqlParameterSource("name", name);

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }

    @Override
    public List<AvailableTimeRow> findAvailableTimesById(Long themeId, LocalDate localDate) {
        String sql = """
                SELECT
                    t.id as time_id,
                    t.start_at as time_start_at,
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

    @Override
    public List<ThemeRow> findPopulars(PopularThemeRequestDto popularThemeRequestDto) {
        String sql = """
                    SELECT
                        th.id AS theme_id,
                        th.name AS theme_name,
                        th.thumbnail_url AS theme_thumbnail_url,
                        th.description AS theme_description
                    FROM themes th
                    LEFT JOIN (
                        SELECT theme_id, COUNT(*) AS cnt
                        FROM reservations
                        WHERE date BETWEEN :start_date AND :end_date
                        GROUP BY theme_id
                    ) r ON th.id = r.theme_id
                    ORDER BY COALESCE(r.cnt, 0) DESC
                    LIMIT :limit
                """;

        SqlParameterSource params = new MapSqlParameterSource("start_date", popularThemeRequestDto.date().minusDays(popularThemeRequestDto.days()))
                .addValue("end_date", popularThemeRequestDto.date())
                .addValue("limit", popularThemeRequestDto.limit());

        return jdbcTemplate.query(sql, params, THEME_ROW_MAPPER);
    }
}
