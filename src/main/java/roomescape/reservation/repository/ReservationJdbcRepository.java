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
import roomescape.reservation.service.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                select r.id as id, 
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

    @Override
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

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                select r.id as id, 
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

    @Override
    public void deleteById(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existSameDateTime(ReservationDate reservationDate, Long timeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE date = ? AND time_id = ?
                )
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, reservationDate.getDate(), timeId);
    }

    @Override
    public boolean existReservationByTimeId(Long timeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1 
                    FROM reservation 
                    WHERE time_id = ?
                )
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    @Override
    public boolean existReservationByThemeId(Long themeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1 
                    FROM reservation 
                    WHERE theme_id = ?
                )
                """;
        int count = jdbcTemplate.queryForObject(sql, Integer.class, themeId);
        return count > 0;
    }

    @Override
    public boolean existsByTimeIdAndDateAndThemeId(Long id, LocalDate date, Long themeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE time_id = ? AND date = ? AND theme_id = ?
                )
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, id, date, themeId);
    }

    @Override
    public int countReservationByThemeIdAndDuration(LocalDate from, LocalDate to, Long themeId) {
        String sql = """
                SELECT count(*)
                FROM reservation
                WHERE theme_id = ?
                AND date >= ?
                ANd date < ?
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, themeId, from, to);
    }
}
