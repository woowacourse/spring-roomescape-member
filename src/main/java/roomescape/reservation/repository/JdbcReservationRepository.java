package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.ReservationTime;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Reservation save(Reservation reservation) {
        String query = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (:name, :date, :time_id, :theme_id)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getThemeId());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(query, params, keyHolder);

        return new Reservation(
                keyHolder.getKey().longValue(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getThemeId()
        );
    }

    public boolean deleteById(final Long id) {
        String query = "DELETE FROM reservation WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        final int updated = jdbcTemplate.update(query, params);

        return updated > 0;
    }

    @Override
    public boolean existsByDateAndTimeId(LocalDate date, Long timeId) {
        String query = """
                SELECT COUNT(*)
                FROM reservation
                WHERE date = :date
                AND time_id = :timeId
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("timeId", timeId);

        Integer count = jdbcTemplate.queryForObject(query, params, Integer.class);
        return count != null && count > 0;
    }

    public List<Reservation> findAll() {
        String query = """
                SELECT 
                r.id as reservation_id, 
                r.name, 
                r.date, 
                t.id as time_id, 
                t.start_at as time_value, 
                r.theme_id 
                FROM reservation as r 
                inner join reservation_time as t 
                on r.time_id = t.id
                """;

        return jdbcTemplate.query(query, (resultSet, rowNum) -> {
            final long id = resultSet.getLong("reservation_id");
            String name = resultSet.getString("name");
            LocalDate date = resultSet.getObject("date", LocalDate.class);
            final long timeId = resultSet.getLong("time_id");
            LocalTime timeValue = resultSet.getObject("time_value", LocalTime.class);
            ReservationTime timeEntity = new ReservationTime(timeId, timeValue);
            final long themeId = resultSet.getLong("theme_id");

            return new Reservation(
                    id,
                    name,
                    date,
                    timeEntity,
                    themeId
            );
        });
    }

    @Override
    public List<Reservation> findAllByTimeId(Long id) {
        String query = """
                SELECT r.id, r.name, r.date, rt.start_at, r.theme_id
                FROM reservation as r
                INNER JOIN reservation_time as rt
                ON r.time_id = rt.id
                WHERE rt.id = :id
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(query, params, (resultSet, rowNum) -> {
            long reservationId = resultSet.getLong("id");
            String name = resultSet.getString("name");
            LocalDate date = resultSet.getObject("date", LocalDate.class);
            LocalTime startAt = resultSet.getObject("start_at", LocalTime.class);
            final long themeId = resultSet.getLong("theme_id");
            ReservationTime timeEntity = new ReservationTime(id, startAt);

            return new Reservation(
                    reservationId,
                    name,
                    date,
                    timeEntity,
                    themeId
            );
        });
    }
}
