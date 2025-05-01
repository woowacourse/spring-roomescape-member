package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.domain.ReserverName;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String sql = """
                select r.id as reservation_id, 
                       r.name,
                       r.date, 
                       t.id as time_id, 
                       t.start_at as time_value, 
                       th.id as theme_id, 
                       th.name as theme_name, 
                       th.description as theme_description, 
                       th.thumbnail as theme_thumbnail
                from reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on r.theme_id = th.id
                """;

        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
                new Reservation(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        LocalDate.parse(resultSet.getString("date")),
                        new ReservationTime(
                                resultSet.getLong("time_id"),
                                LocalTime.parse(resultSet.getString("time_value"))
                        ),
                        new Theme(
                                resultSet.getLong("theme_id"),
                                resultSet.getString("theme_name"),
                                resultSet.getString("theme_description"),
                                resultSet.getString("theme_thumbnail")
                        )));
    }

    public Reservation save(Reservation reservation) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reservation.getReserverName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTimeId());
        Long id = jdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Reservation(id, reservation.getReserverName(), reservation.getDate(),
                new ReservationTime(reservation.getTimeId(), reservation.getStartAt()),
                reservation.getTheme());
    }

    public Reservation save(ReserverName reserverName, ReservationDateTime reservationDateTime, Theme theme) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reserverName.getName())
                .addValue("date", reservationDateTime.getReservationDate().getDate())
                .addValue("time_id", reservationDateTime.getReservationTime().getId())
                .addValue("theme_id", theme.getId());
        Long id = jdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Reservation(id, reserverName.getName(), reservationDateTime.getReservationDate().getDate(),
                new ReservationTime(reservationDateTime.getReservationTime().getId(),
                        reservationDateTime.getReservationTime().getStartAt()),
                theme);
    }

    public Optional<Reservation> findById(Long id) {
        String sql = """
                select r.id as reservation_id, 
                       r.name,
                       r.date, 
                       t.id as time_id, 
                       t.start_at as time_value, 
                       th.id as theme_id, 
                       th.name as theme_name, 
                       th.description as theme_description, 
                       th.thumbnail as theme_thumbnail
                from reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on r.theme_id = th.id
                where r.id = ?
                """;

        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
                        new Reservation(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                LocalDate.parse(resultSet.getString("date")),
                                new ReservationTime(
                                        resultSet.getLong("time_id"),
                                        LocalTime.parse(resultSet.getString("time_value"))
                                ),
                                new Theme(
                                        resultSet.getLong("theme_id"),
                                        resultSet.getString("theme_name"),
                                        resultSet.getString("theme_description"),
                                        resultSet.getString("theme_thumbnail")
                                )), id)
                .stream()
                .findFirst();
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existSameDateTime(ReservationDate reservationDate, Long timeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE date = ? AND time_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, reservationDate.getDate(), timeId);
        return count > 0;
    }

    public boolean existReservationByTimeId(Long timeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, timeId);
        return count > 0;
    }

    public boolean existReservationByThemeId(Long themeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE theme_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, themeId);
        return count > 0;
    }

}
