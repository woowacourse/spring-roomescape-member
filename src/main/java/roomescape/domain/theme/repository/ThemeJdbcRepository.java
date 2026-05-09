package roomescape.domain.theme.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.response.ThemeReservationTimeResponse;

@Repository
public class ThemeJdbcRepository implements ThemeRepository {

    private static final String FIND_ALL_THEMES_QUERY = """
            SELECT *
            FROM theme;
            """;

    private static final String FIND_THEME_BY_ID_QUERY = """
            SELECT *
            FROM theme
            WHERE id = :id;
            """;

    private static final String FIND_ALL_THEME_RESERVATION_TIMES_BY_THEME_ID_AND_DATE_QUERY = """
            SELECT
                rt.id,
                rt.start_at,
                NOT EXISTS (
                    SELECT 1
                    FROM reservation r
                    WHERE r.time_id = rt.id
                      AND r.theme_id = :themeId
                      AND r.date = :date
                ) AS is_available
            FROM reservation_time AS rt
            ORDER BY rt.start_at ASC;
            """;

    private static final String DELETE_THEME_BY_ID_QUERY = """
            DELETE FROM theme
            WHERE id = :id;
            """;

    private static final String FIND_POPULAR_THEMES_QUERY = """
            SELECT
                t.id,
                t.name,
                t.description,
                t.thumbnail_url,
                RANK() OVER (ORDER BY COUNT(r.id) DESC) AS rank
            FROM theme t
            LEFT JOIN reservation r ON r.theme_id = t.id
               AND r.date BETWEEN :startDate AND :endDate
            GROUP BY t.id, t.name, t.description, t.thumbnail_url
            ORDER BY rank, t.id
            LIMIT :limit;
            """;

    private static final RowMapper<Theme> THEME_ROW_MAPPER = (resultSet, rowNumber) -> Theme.of(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail_url")
    );

    private static final RowMapper<ThemeReservationTimeResponse> THEME_RESERVATION_TIME_RESPONSE_ROW_MAPPER = (resultSet, rowNumber) -> new ThemeReservationTimeResponse(
            resultSet.getLong("id"),
            resultSet.getObject("start_at", LocalTime.class),
            resultSet.getBoolean("is_available")
    );

    private static final RowMapper<PopularThemeResult> POPULAR_THEME_RESULT_ROW_MAPPER = (resultSet, rowNumber) -> new PopularThemeResult(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail_url"),
            resultSet.getInt("rank")
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_THEMES_QUERY,
                THEME_ROW_MAPPER
        );
    }

    @Override
    public Optional<Theme> findById(Long id) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("id", id);

            Theme theme = jdbcTemplate.queryForObject(
                    FIND_THEME_BY_ID_QUERY,
                    parameters,
                    THEME_ROW_MAPPER
            );

            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<ThemeReservationTimeResponse> findAllThemeReservationTimesByThemeIdAndDate(
            Long themeId,
            LocalDate date
    ) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("themeId", themeId)
                .addValue("date", date);

        return jdbcTemplate.query(
                FIND_ALL_THEME_RESERVATION_TIMES_BY_THEME_ID_AND_DATE_QUERY,
                parameters,
                THEME_RESERVATION_TIME_RESPONSE_ROW_MAPPER
        );
    }

    @Override
    public List<PopularThemeResult> findPopularThemes(
            LocalDate startDate,
            LocalDate endDate,
            Integer limit
    ) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("startDate", startDate)
                .addValue("endDate", endDate)
                .addValue("limit", limit);

        return jdbcTemplate.query(
                FIND_POPULAR_THEMES_QUERY,
                parameters,
                POPULAR_THEME_RESULT_ROW_MAPPER
        );
    }

    @Override
    public Theme save(Theme theme) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail_url", theme.getThumbnailUrl());

        Number key = simpleJdbcInsert.executeAndReturnKey(parameters);
        theme.assignId(key.longValue());

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
}
