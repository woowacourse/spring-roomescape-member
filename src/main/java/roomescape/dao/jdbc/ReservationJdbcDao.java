package roomescape.dao.jdbc;

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
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.vo.Name;

@Repository
public class ReservationJdbcDao implements ReservationDao {
    private static final RowMapper<Theme> THEME_ROW_MAPPER = (rs, rowNum) ->
            new Theme(
                    rs.getLong("theme_id"),
                    new Name(rs.getString("theme_name")),
                    rs.getString("theme_thumbnail_url"),
                    rs.getString("theme_description")
            );
    private static final RowMapper<Time> TIME_ROW_MAPPER = (resultSet, rowNum) ->
            new Time(
                    resultSet.getLong("time_id"),
                    LocalTime.parse(resultSet.getString("time_start_at"))
            );
    private static final RowMapper<Reservation> ROW_MAPPER = (rs, rowNum) ->
            new Reservation(
                    rs.getLong("id"),
                    rs.getString("name"),
                    LocalDate.parse(rs.getString("date")),
                    TIME_ROW_MAPPER.mapRow(rs, rowNum),
                    THEME_ROW_MAPPER.mapRow(rs, rowNum)
            );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationJdbcDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("reservations")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "date", "time_id", "theme_id");
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.thumbnail_url AS theme_thumbnail_url,
                    th.description AS theme_description
                FROM reservations r
                INNER JOIN times t ON r.time_id = t.id
                INNER JOIN themes th ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.thumbnail_url AS theme_thumbnail_url,
                    th.description AS theme_description
                FROM reservations r
                INNER JOIN times t ON r.time_id = t.id
                INNER JOIN themes th ON r.theme_id = th.id
                WHERE r.id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(sql, params, ROW_MAPPER).stream()
                .findFirst();
    }

    @Override
    public Reservation insert(Reservation reservation) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());

        Long id = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        return new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public int delete(Long id) {
        String sql = """
                DELETE FROM reservations
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.update(sql, params);
    }

    @Override
    public boolean existsByThemeIdAndTimeIdAndDate(Long themeId, Long timeId, LocalDate date) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1 FROM reservations r
                    WHERE r.theme_id = :themeId
                    AND r.time_id = :timeId
                    AND r.date = :date
                )
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("themeId", themeId)
                .addValue("timeId", timeId)
                .addValue("date", date);

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));

    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1 FROM reservations r
                    WHERE r.theme_id = :themeId
                )
                """;
        SqlParameterSource params = new MapSqlParameterSource("themeId", themeId);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1 FROM reservations r
                    WHERE r.time_id = :timeId
                )
                """;
        SqlParameterSource params = new MapSqlParameterSource("timeId", timeId);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }
}
