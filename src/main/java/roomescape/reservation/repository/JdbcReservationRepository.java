package roomescape.reservation.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.entity.Reservation;
import roomescape.time.entity.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) -> {
        final long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        LocalDate date = resultSet.getObject("date", LocalDate.class);
        final long timeId = resultSet.getLong("time_id");
        LocalTime timeValue = resultSet.getObject("start_at", LocalTime.class);
        ReservationTime timeEntity = ReservationTime.of(timeId, timeValue);
        final long themeId = resultSet.getLong("theme_id");
        return Reservation.of(
                id,
                name,
                date,
                timeEntity,
                themeId
        );
    };
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Reservation save(Reservation newReservation) {
        String query = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (:name, :date, :time_id, :theme_id)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", newReservation.getName())
                .addValue("date", newReservation.getDate())
                .addValue("time_id", newReservation.getTimeId())
                .addValue("theme_id", newReservation.getThemeId());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, params, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return Reservation.of(
                id,
                newReservation.getName(),
                newReservation.getDate(),
                newReservation.getTime(),
                newReservation.getThemeId()
        );
    }

    public boolean deleteById(final Long id) {
        String query = "DELETE FROM reservation WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        final int updated = jdbcTemplate.update(query, params);
        return updated > 0;
    }

    public List<Reservation> findAll() {
        String query = """
                SELECT 
                    r.id, 
                    r.name, 
                    r.date, 
                    t.id as time_id, 
                    t.start_at, 
                    r.theme_id 
                FROM reservation as r 
                inner join reservation_time as t 
                on r.time_id = t.id
                """;
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    @Override
    public List<Reservation> findAllByTimeId(Long id) {
        String query = """
                SELECT 
                    r.id, 
                    r.name, 
                    r.date, 
                    rt.start_at, 
                    r.theme_id, 
                    rt.id as time_id
                FROM reservation as r
                INNER JOIN reservation_time as rt
                ON r.time_id = rt.id
                WHERE rt.id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.query(query, params, ROW_MAPPER);
    }

    // TODO: (모든 Repository, Service에 적용) try-catch 삭제 및 Service에서 DataAccessException 핸들링하도록
    @Override
    public Optional<Reservation> findDuplicatedWith(Reservation entity) {
        String query = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    rt.start_at,
                    r.theme_id,
                    rt.id as time_id
                FROM reservation as r
                JOIN reservation_time as rt
                ON r.time_id = rt.id
                WHERE r.theme_id = :themeId AND r.date = :date AND rt.start_at = :startTime
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("themeId", entity.getThemeId())
                .addValue("date", entity.getDate())
                .addValue("startTime", entity.getStartAt());
        try {
            Reservation reservation = jdbcTemplate.queryForObject(query, params, ROW_MAPPER);
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
