package roomescape.reservation.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        ReservationTime time = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getTime("start_at").toLocalTime()
        );

        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_thumbnail_url")
        );

        return new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("reservation_name"),
                resultSet.getDate("reservation_date").toLocalDate(),
                time,
                theme
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(Reservation reservation) {
        String sql = "insert into reservation (name, reservation_date, time_id, theme_id) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from reservation where id = ?", id);
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "select " +
                "r.id as reservation_id, " +
                "r.name as reservation_name, " +
                "r.reservation_date, " +
                "r.time_id, " +
                "t.start_at, " +
                "h.id as theme_id, " +
                "h.name as theme_name, " +
                "h.description as theme_description, " +
                "h.thumbnail_url as theme_thumbnail_url " +
                "from reservation r " +
                "inner join reservation_time t on r.time_id = t.id " +
                "inner join theme h on r.theme_id = h.id";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = "select exists (select 1 from reservation where reservation_date = ? and time_id = ? and theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }
}
