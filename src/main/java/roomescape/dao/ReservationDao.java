package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

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
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM 
                    reservation AS r 
                INNER JOIN 
                    reservation_time AS t 
                ON 
                    r.time_id = t.id
                INNER JOIN 
                    theme AS th
                ON
                    r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, this::rowMapper);
    }

    public List<Reservation> selectAllByDateAndTime(LocalDate date, ReservationTime time, Long themeId) {
        String sql = """
                SELECT 
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM 
                    reservation AS r 
                INNER JOIN 
                    reservation_time AS t 
                ON 
                    r.time_id = t.id
                INNER JOIN 
                    theme AS th
                ON
                    r.theme_id = th.id
                WHERE 
                    `date` = ? AND t.start_at = ? AND th.id = ?
                """;
        return jdbcTemplate.query(sql, this::rowMapper, Date.valueOf(date), Time.valueOf(time.getStartAt()), themeId);
    }

    public Long insert(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getReservationTimeId())
                .addValue("theme_id", reservation.getThemeId());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return Objects.requireNonNull(id);
    }

    public Boolean existById(Long id) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE id = ?
                ) AS is_exist;
                """;
        return jdbcTemplate.queryForObject(sql,
                (resultSet, rowNumber) -> resultSet.getBoolean("is_exist"), id);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public int countByTimeId(Long timeId) {
        String sql = "SELECT count(*) FROM reservation WHERE time_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, timeId);
    }

    public List<Long> selectAllTimeIdsByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT 
                    time_id
                FROM 
                    reservation 
                WHERE 
                    date = ? AND theme_id = ?
                """;
        return jdbcTemplate.query(sql,
                (resultSet, rowNumber) -> resultSet.getLong("time_id"),
                Date.valueOf(date), themeId);
    }

    private Reservation rowMapper(ResultSet resultSet, int rowNumber) throws SQLException {
        ReservationTime reservationTime = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getString("time_value")
        );
        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_thumbnail")
        );
        return reservationMapper(resultSet, reservationTime, theme);
    }

    private Reservation reservationMapper(ResultSet resultSet, ReservationTime reservationTime, Theme theme) throws SQLException {
        return new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                resultSet.getString("date"),
                reservationTime,
                theme
        );
    }
}
