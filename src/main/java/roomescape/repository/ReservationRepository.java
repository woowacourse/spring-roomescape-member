package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(resultSet.getLong("reservation_time_id"),
                    resultSet.getTime("time_value").toLocalTime()),
            new Theme(resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            )
    );

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String sql = "SELECT " +
                "    r.id as reservation_id, " +
                "    r.name, " +
                "    r.date, " +
                "    t.id as reservation_time_id, " +
                "    t.start_at as time_value, " +
                "    th.id as theme_id, " +
                "    th.name as theme_name, " +
                "    th.description as theme_description, " +
                "    th.thumbnail as theme_thumbnail " +
                "FROM reservation as r " +
                "inner join reservation_time as t " +
                "on r.reservation_time_id = t.id " +
                "inner join theme as th " +
                "on r.theme_id = th.id";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, reservation_time_id, theme_id) values (?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql,
                    new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getReservationTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        return new Reservation(keyHolder.getKey().longValue(), reservation.getName(),
                reservation.getDate(), reservation.getReservationTime(), reservation.getTheme());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByReservationTimeId(Long reservationTimeId) {
        String sql = "SELECT exists(SELECT 1 FROM reservation " +
                "where reservation_time_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, reservationTimeId);
    }

    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long reservationTimeId, Long themeId) {
        String sql = "SELECT exists(SELECT 1 FROM reservation " +
                "where date = ? and reservation_time_id = ? and theme_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, Date.valueOf(date), reservationTimeId, themeId);
    }
}
