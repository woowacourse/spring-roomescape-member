package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.model.ReservationDateTime;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.model.UserName;

@Repository
public class JdbcReservationRepository implements ReservationRepository, ReservedTimeChecker, ReservedThemeChecker,
        ReservedChecker {
    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> getAllReservations() {
        String sql = """
                SELECT r.id, r.name, r.date, r.time_id, t.start_at, r.theme_id, th.name AS theme_name, th.description, th.thumbnail
                FROM reservation as r inner join reservation_time as t on r.time_id = t.id 
                inner join theme as th on r.theme_id = th.id""";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            System.out.println(rs.getString("name"));
            UserName userName = new UserName(rs.getString("name"));
            ReservationDateTime dateTime = new ReservationDateTime(LocalDate.parse(rs.getString("date")),
                    new ReservationTime(rs.getLong("time_id"), rs.getTime("start_at").toLocalTime()));
            Theme theme = new Theme(rs.getLong("theme_id"), rs.getString("theme_name"), rs.getString("description"),
                    rs.getString("thumbnail"));

            return new Reservation(rs.getLong("id"), userName, dateTime, theme);
        });
    }

    @Override
    public Reservation addReservation(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql, new String[]{"id"});
            ps.setString(1, reservation.getUserName().getName());
            ps.setString(2, reservation.getReservationDateTime().getDate().toString());
            ps.setLong(3, reservation.getReservationDateTime().getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);
        return new Reservation(Objects.requireNonNull(keyHolder.getKey()).longValue(), reservation.getUserName(),
                reservation.getReservationDateTime(), reservation.getTheme());
    }

    @Override
    public int deleteReservation(Long id) {
        return jdbcTemplate.update("delete from reservation where id = ?", id);
    }

    @Override
    public boolean contains(LocalDate reservationDate, Long timeId, Long themeId) {
        String sql = "select exists (select 1 from reservation where date = ? and time_id = ? and theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, reservationDate, timeId, themeId);
    }

    @Override
    public boolean isReservedTime(Long timeId) {
        String sql = "select exists (select 1 from reservation where time_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    @Override
    public boolean isReservedTheme(Long themeId) {
        String sql = "select exists (select 1 from reservation where theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
    }

    @Override
    public List<Long> getBestThemesIdInDays(LocalDate startDate, LocalDate endDate) {
        String sql = "select theme_id from reservation where date between ? and ? group by theme_id order by count(*) desc limit 10";

        List<Long> longs = jdbcTemplate.queryForList(
                sql,
                Long.class,
                startDate.toString(),
                endDate.toString()
        );
        return longs;
    }
}
