package roomescape.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
public class ReservationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> selectAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value
                FROM
                    reservation AS r
                INNER JOIN
                    reservation_time AS t
                ON
                    r.time_id = t.id
                """;
        return jdbcTemplate.query(sql, this::rowMapper);
    }

    public List<Reservation> selectAllByDateAndTime(LocalDate date, ReservationTime time) {
        String sql = """
                SELECT 
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value
                FROM 
                    reservation AS r 
                INNER JOIN 
                    reservation_time AS t 
                ON 
                    r.time_id = t.id 
                WHERE 
                    `date` = ? AND t.start_at = ?
                """;
        return jdbcTemplate.query(sql, this::rowMapper, Date.valueOf(date), Time.valueOf(time.getStartAt()));
    }

    public Long insert(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getReservationTimeId());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return Objects.requireNonNull(id);
    }

    public Reservation selectById(Long id) {
        try {
            String sql = "SELECT id AS reservation_id, name, date, time_id FROM reservation WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, this::lazyRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private Reservation rowMapper(ResultSet resultSet, int rowNumber) throws SQLException {
        ReservationTime reservationTime = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getTime("time_value").toLocalTime()
        );
        return reservationMapper(resultSet, reservationTime);
    }

    private Reservation lazyRowMapper(ResultSet resultSet, int rowNumber) throws SQLException {
        ReservationTime reservationTime = new ReservationTime(resultSet.getLong("time_id"));
        return reservationMapper(resultSet, reservationTime);
    }

    private Reservation reservationMapper(ResultSet resultSet, ReservationTime reservationTime) throws SQLException {
        return new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                reservationTime
        );
    }
}
