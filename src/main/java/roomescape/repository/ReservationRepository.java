package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationDateTime;
import roomescape.domain.reservation.ReserverName;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;
import roomescape.domain.time.ReservationTime;

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
                                new ThemeName(resultSet.getString("theme_name")),
                                new ThemeDescription(resultSet.getString("theme_description")),
                                new ThemeThumbnail(resultSet.getString("theme_thumbnail"))
                        )));
    }

    public Reservation save(ReserverName reserverName, ReservationDateTime reservationDateTime, Theme theme) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reserverName.getName())
                .addValue("date", reservationDateTime.getReservationDate().date())
                .addValue("time_id", reservationDateTime.getReservationTime().getId())
                .addValue("theme_id", theme.getId());
        Long id = jdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Reservation(id, reserverName.getName(), reservationDateTime.getReservationDate().date(),
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
                                        new ThemeName(resultSet.getString("theme_name")),
                                        new ThemeDescription(resultSet.getString("theme_description")),
                                        new ThemeThumbnail(resultSet.getString("theme_thumbnail"))
                                )), id)
                .stream()
                .findFirst();
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existSameDateTime(ReservationDate reservationDate, Long timeId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = ? AND time_id = ?)";
        return jdbcTemplate.queryForObject(sql, boolean.class, reservationDate.date(), timeId);
    }

    public boolean existReservationByTimeId(Long timeId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = ?)";
        return jdbcTemplate.queryForObject(sql, boolean.class, timeId);
    }

    public boolean existReservationByThemeId(Long themeId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, boolean.class, themeId);
    }

}
