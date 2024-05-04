package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Reservations;
import roomescape.domain.Theme;

@Repository
public class JdbcTemplateReservationRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (rs, rowNum) -> new Reservation(
            rs.getLong("reservation_id"),
            rs.getString("reservation_name"),
            rs.getDate("reservation_date").toLocalDate(),
            new ReservationTime(
                    rs.getLong("time_id"),
                    rs.getTime("time_value").toLocalTime()
            ),
            new Theme(
                    rs.getLong("theme_id"),
                    rs.getString("theme_name"),
                    rs.getString("description"),
                    rs.getString("thumbnail")
            )
    );

    public JdbcTemplateReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationTime reservationTime = findReservationTime(reservation.getReservationTime().getId());
        Reservation beforeSaved = new Reservation(null, reservation.getName(), reservation.getDate(),
                reservationTime, reservation.getTheme());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        save(beforeSaved, keyHolder);
        long id = keyHolder.getKey().longValue();
        return new Reservation(id, reservation);
    }

    private ReservationTime findReservationTime(long timeId) {
        String reservationTimeSelectSql = "select * from reservation_time where id = ?";
        return jdbcTemplate.queryForObject(reservationTimeSelectSql, (rs, rowNum) -> {
            long id = rs.getLong(1);
            LocalTime startAt = rs.getTime(2).toLocalTime();
            return new ReservationTime(id, startAt);
        }, timeId);
    }

    private void save(Reservation reservation, KeyHolder keyHolder) {
        jdbcTemplate.update(con -> {
            String sql = "insert into reservation(name,date,time_id,THEME_ID) values ( ?,?,?,? )";
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setDate(2, Date.valueOf(reservation.getDate()));
            preparedStatement.setLong(3, reservation.getReservationTime().getId());
            preparedStatement.setLong(4, reservation.getTheme().getId());
            return preparedStatement;
        }, keyHolder);
    }

    @Override
    public Reservations findAll() {
        String query = """
                   SELECT 
                   r.id as reservation_id,
                   r.name as reservation_name,
                   r.date as reservation_date,
                   t.id as time_id,
                   t.start_at as time_value,
                   t2.id as theme_id,
                   t2.NAME as theme_name,
                   t2.DESCRIPTION as description,
                   t2.THUMBNAIL as thumbnail
                FROM reservation as r
                inner join reservation_time t 
                on r.time_id = t.id
                inner join theme t2  
                on t2.id = r.theme_id""";

        List<Reservation> findReservations = jdbcTemplate.query(query, RESERVATION_ROW_MAPPER);
        return new Reservations(findReservations);
    }

    @Override
    public Reservations findByThemeAndDate(Theme theme, LocalDate date) {
        String query = """
                   SELECT 
                   r.id as reservation_id,
                   r.name as reservation_name,
                   r.date as reservation_date,
                   t.id as time_id,
                   t.start_at as time_value,
                   t2.id as theme_id,
                   t2.NAME as theme_name,
                   t2.DESCRIPTION as description,
                   t2.THUMBNAIL as thumbnail
                FROM reservation as r
                inner join reservation_time t 
                on r.time_id = t.id
                inner join theme t2  
                on t2.id = r.theme_id
                WHERE theme_id = ? AND r.date = ?; 
                """;

        List<Reservation> findReservations = jdbcTemplate.query(query, RESERVATION_ROW_MAPPER, theme.getId(),
                Date.valueOf(date));
        return new Reservations(findReservations);
    }

    @Override
    public boolean existByTimeId(long timeId) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT time_id FROM RESERVATION WHERE time_id = ?)",
                Boolean.class, timeId
        );
    }

    @Override
    public boolean existByThemeId(long themeId) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT theme_id FROM RESERVATION WHERE theme_id = ?)",
                Boolean.class, themeId
        );
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("delete from reservation where id = ?", id);
    }
}
