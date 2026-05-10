package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationQueryingDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ReservationQueryingDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        ReservationTime reservationTime = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getObject("start_at", LocalTime.class)
        );

        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_url")
        );

        return new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("reservation_name"),
                resultSet.getObject("reservation_date", LocalDate.class),
                reservationTime,
                theme,
                resultSet.getObject("reservation_created_at", LocalDateTime.class)
        );
    };

    public Optional<Reservation> findReservationById(long id) {
        String sql = """
                select r.id as reservation_id, r.name as reservation_name, r.date as reservation_date, r.time_id, r.created_at as reservation_created_at, t.start_at, th.id as theme_id, th.name as theme_name, th.description as theme_description, th.url as theme_url
                from reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on th.id = r.theme_id
                where r.id = :id
                """;
        try {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("id", id);
            Reservation reservation = jdbcTemplate.queryForObject(sql, param, reservationRowMapper);
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<Reservation> findAllReservations() {
        String sql = """
                select r.id as reservation_id, r.name as reservation_name, r.date as reservation_date, r.time_id, r.created_at as reservation_created_at, t.start_at, th.id as theme_id, th.name as theme_name, th.description as theme_description, th.url as theme_url
                from reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on th.id = r.theme_id
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Optional<Reservation> findReservationByThemeAndDateAndTime(Long themeId, LocalDate date, Long timeId) {
        String sql = """
                select r.id as reservation_id, r.name as reservation_name, r.date as reservation_date, r.time_id, r.created_at as reservation_created_at, t.start_at, th.id as theme_id, th.name as theme_name, th.description as theme_description, th.url as theme_url
                from reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on th.id = r.theme_id
                where r.theme_id = :theme_id and r.date = :date and r.time_id = :time_id
                """;
        try {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("theme_id", themeId)
                    .addValue("date", date)
                    .addValue("time_id", timeId);
            Reservation reservation = jdbcTemplate.queryForObject(sql, param, reservationRowMapper);
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }
}
