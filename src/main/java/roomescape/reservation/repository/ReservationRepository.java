package roomescape.reservation.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReservationRepository {
    private static final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("reservation_name"),
                    resultSet.getDate("date").toLocalDate(),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            resultSet.getTime("time_value").toLocalTime()
                    ),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail")
                    ));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        final String sql = """
                SELECT r.id AS reservation_id, r.name AS reservation_name, r.date, rt.id AS time_id,\s
                rt.start_at AS time_value, t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail\s
                FROM reservation r\s
                INNER JOIN reservation_time rt ON r.time_id = rt.id\s
                INNER JOIN theme t ON r.theme_id = t.id""";

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Reservation findById(final long id) {
        final String sql = """
                SELECT r.id AS reservation_id, r.name AS reservation_name, r.date,\s
                rt.id AS time_id, rt.start_at AS time_value,\s
                t.id AS theme_id, t.name AS theme_name, t.description, t.thumbnail\s
                FROM reservation r\s
                INNER JOIN reservation_time rt ON r.time_id = rt.id\s
                INNER JOIN theme t ON r.theme_id = t.id\s
                WHERE r.id = ?""";

        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
    }

    public Long save(final Reservation reservation) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Integer deleteById(final long id) {
        final String sql = """
                DELETE FROM reservation WHERE id = ?""";

        return jdbcTemplate.update(sql, id);
    }

    public Boolean checkReservationExists(Reservation reservation) {
        final String sql = """
                SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END\s
                FROM reservation AS r\s
                INNER JOIN reservation_time AS rt\s
                ON r.time_id = rt.id\s
                INNER JOIN theme AS t\s
                ON r.theme_id = t.id\s
                WHERE r.date = ? AND rt.id = ? AND t.id = ?""";

        String date = reservation.getDate().toString();
        Long timeId = reservation.getTime().getId();
        Long themeId = reservation.getTheme().getId();
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }
}
