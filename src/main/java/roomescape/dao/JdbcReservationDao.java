package roomescape.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.ReservationEntity;
import roomescape.entity.ReservationTimeEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class JdbcReservationDao implements ReservationDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcReservationDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ReservationEntity save(ReservationEntity newReservation) {
        String query = "INSERT INTO reservation (name, date, time_id) VALUES (:name, :date, :time_id)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", newReservation.getName())
                .addValue("date", newReservation.getFormattedDate())
                .addValue("time_id", newReservation.getTimeId());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, params, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new ReservationEntity(
                id,
                newReservation.getName(),
                newReservation.getDate(),
                newReservation.getTime()
        );
    }

    public boolean deleteById(final Long id) {
        String query = "DELETE FROM reservation WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        final int updated = jdbcTemplate.update(query, params);
        return updated > 0;
    }

    public List<ReservationEntity> findAll() {
        String query = """
            SELECT
                r.id as reservation_id,
                r.name,
                r.date,
                t.id as time_id,
                t.start_at as time_value
            FROM reservation as r
            inner join reservation_time as t
            on r.time_id = t.id
            """;
        return jdbcTemplate.query(query, (resultSet, rowNum) -> {
            LocalDate date = resultSet.getObject("date", LocalDate.class);
            final long timeId = resultSet.getLong("time_id");
            LocalTime timeValue = resultSet.getObject("time_value", LocalTime.class);
            ReservationTimeEntity timeEntity = new ReservationTimeEntity(timeId, timeValue);
            return new ReservationEntity(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("name"),
                    date,
                    timeEntity
            );
        });
    }

    @Override
    public List<ReservationEntity> findAllByTimeId(Long id) {
        String query = """
                SELECT r.id, r.name, r.date, rt.start_at
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
            return new ReservationEntity(
                    reservationId,
                    name,
                    date,
                    new ReservationTimeEntity(id, startAt)
            );
        });
    }
}
