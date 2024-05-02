package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Repository
public class ReservationDao {

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getObject("date", LocalDate.class),
            new ReservationTime(resultSet.getLong("time_id"), resultSet.getObject("start_at", LocalTime.class)),
            new Theme(resultSet.getLong("theme_id"), resultSet.getString("name"), resultSet.getString("description"), resultSet.getString("thumbnail"))
    );

    private final RowMapper<ReservationTime> timeRowMapper = (resultSet, rowNum) -> new ReservationTime(
            resultSet.getLong("id"),
            LocalTime.parse(resultSet.getString("start_at"))
    );
    private final JdbcTemplate jdbcTemplate;
    private RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate().toString());
            ps.setLong(3, reservation.getReservationTimeId());
            ps.setLong(4, reservation.getThemeId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findAll() {
        List<Reservation> reservations = jdbcTemplate.query("""
                SELECT
                r.id as reservation_id,
                r.name,
                r.date,
                t.id as time_id,
                t.start_at as time_value,
                th.id as theme_id,
                th.name,
                th.description,
                th.thumbnail
                FROM reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on r.theme_id = th.id
                """, reservationRowMapper);
        return Collections.unmodifiableList(reservations);
    }


    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public boolean existByDateTimeTheme(LocalDate date, LocalTime time, Long themeId) {
        int count = jdbcTemplate.queryForObject("""
                SELECT count(*) 
                FROM reservation as r 
                INNER JOIN reservation_time as t ON r.time_id = t.id
                WHERE r.date = ? AND t.start_at = ? AND r.theme_id = ?
                """, Integer.class, date, time, themeId);
        return count > 0;
    }

    public List<Long> findTimeIdByDateThemeId(LocalDate date, Long themeId) {
        return jdbcTemplate.queryForList("SELECT time_id FROM reservation WHERE date = ? AND theme_id = ?", Long.class, date, themeId);
        //todo : reservation 테이블 vs time 테이블
    }

    // TODO: ThemeDao vs ReservationDao
    public List<Theme> findThemesByDescOrder() {
        String nowDate = LocalDate.now().toString();
        String weekBeforeDate = LocalDate.now().minusDays(7).toString();
        return jdbcTemplate.query("""
                SELECT
                th.id as theme_id,
                th.name,
                th.description,
                th.thumbnail,
                COUNT(r.theme_id) AS reservation_count
                FROM reservation AS r
                INNER JOIN theme AS th ON r.theme_id = th.id
                WHERE r.date < ? AND r.date > ?
                GROUP BY th.id, th.name, th.description, th.thumbnail
                ORDER BY reservation_count DESC
                LIMIT 10
                """, themeRowMapper, nowDate, weekBeforeDate);
    }
}
