package roomescape.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.row.ReservationRow;
import roomescape.dao.row.ThemeRow;
import roomescape.dao.row.TimeRow;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationJdbcDao implements ReservationDao {
    private static final RowMapper<ReservationRow> ROW_MAPPER = (rs, rowNum) ->
            new ReservationRow(
                    rs.getLong("reservation_id"),
                    rs.getString("reservation_name"),
                    rs.getDate("reservation_date").toLocalDate(),
                    new TimeRow(
                            rs.getLong("time_id"),
                            rs.getTime("time_start_at").toLocalTime()
                    ),
                    new ThemeRow(
                            rs.getLong("theme_id"),
                            rs.getString("theme_name"),
                            rs.getString("theme_thumbnail_url"),
                            rs.getString("theme_description")
                    )
            );


    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert reservationInsert;

    public ReservationJdbcDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("reservations")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "date", "time_id", "theme_id");
    }

    @Override
    public List<ReservationRow> findAll() {
        String sql = """
                 SELECT
                    r.id AS reservation_id,
                    r.name AS reservation_name,
                    r.date AS reservation_date,
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
    public Optional<ReservationRow> findById(Long id) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name AS reservation_name,
                    r.date AS reservation_date,
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
    public ReservationRow create(ReservationRow reservation) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", reservation.name())
                .addValue("date", reservation.date())
                .addValue("time_id", reservation.timeRow().id())
                .addValue("theme_id", reservation.themeRow().id());

        Long id = reservationInsert.executeAndReturnKey(sqlParameterSource).longValue();
        return new ReservationRow(
                id,
                reservation.name(),
                reservation.date(),
                reservation.timeRow(),
                reservation.themeRow()
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
                    WHERE r.theme_id = :theme_id
                    AND r.time_id = :time_id
                    AND r.date = :date
                    )
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("theme_id", themeId)
                .addValue("time_id", timeId)
                .addValue("date", date);

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1 FROM reservations r
                    WHERE r.theme_id = :theme_id
                )
                """;
        SqlParameterSource params = new MapSqlParameterSource("theme_id", themeId);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1 FROM reservations r
                    WHERE r.time_id = :time_id
                )
                """;
        SqlParameterSource params = new MapSqlParameterSource("time_id", timeId);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }
}
