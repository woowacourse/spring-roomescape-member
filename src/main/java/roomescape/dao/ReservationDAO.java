package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ReservationDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationDAO(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public Reservation insert(Reservation reservation) {
        final String name = reservation.getName();
        final LocalDate date = reservation.getDate();
        final ReservationTime time = reservation.getTime();
        final Theme theme = reservation.getTheme();

        final SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("date", date)
                .addValue("time_id", time.getId())
                .addValue("theme_id", theme.getId());

        final long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new Reservation(id, name, date, time, theme);
    }

    public List<Reservation> selectAll() {
        final String sql =
                "SELECT " +
                        "r.id AS reservation_id, " +
                        "r.name, " +
                        "r.date, " +
                        "rt.id AS time_id, " +
                        "rt.start_at AS time_value, " +
                        "t.id AS theme_id, " +
                        "t.name AS theme_name, " +
                        "t.description AS theme_description, " +
                        "t.thumbnail AS theme_thumbnail " +
                        "FROM reservation AS r " +
                        "INNER JOIN reservation_time AS rt " +
                        "ON r.time_id = rt.id " +
                        "INNER JOIN theme AS t " +
                        "ON r.theme_id = t.id";

        return jdbcTemplate.query(sql, reservationRowMapper());
    }

    public void deleteById(long id) {
        final String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean hasReservationTime(Long timeId) {
        final String sql =
                "SELECT " +
                        "r.id AS reservation_id, " +
                        "r.name, " +
                        "r.date, " +
                        "rt.id AS time_id, " +
                        "rt.start_at AS time_value, " +
                        "t.id AS theme_id, " +
                        "t.name AS theme_name, " +
                        "t.description AS theme_description, " +
                        "t.thumbnail AS theme_thumbnail " +
                        "FROM reservation AS r " +
                        "INNER JOIN reservation_time AS rt " +
                        "ON r.time_id = rt.id " +
                        "INNER JOIN theme AS t " +
                        "ON r.theme_id = t.id " +
                        "WHERE time_id = ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationRowMapper(), timeId);

        return !reservations.isEmpty();
    }

    public List<Long> findReservedTimeIds(LocalDate date, Long themeId) {
        final String sql = "SELECT time_id FROM reservation WHERE date = ? AND theme_id = ?";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getLong("time_id"), date, themeId);
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("time_value").toLocalTime()
                ),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("theme_description"),
                        resultSet.getString("theme_thumbnail")
                )
        );
    }
}
