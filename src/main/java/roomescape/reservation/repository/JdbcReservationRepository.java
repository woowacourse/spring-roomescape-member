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
import roomescape.reservation.repository.dto.ReservationWithFilterRequest;
import roomescape.time.entity.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private final RowMapper<Reservation> ROW_MAPPER = (resultSet, rowNum) -> {
        final long timeId = resultSet.getLong("time_id");
        LocalTime timeValue = resultSet.getObject("start_at", LocalTime.class);
        ReservationTime timeEntity = new ReservationTime(timeId, timeValue);
        return new Reservation(
                resultSet.getLong("id"),
                resultSet.getLong("member_id"),
                resultSet.getObject("date", LocalDate.class),
                timeEntity,
                resultSet.getLong("theme_id")
        );
    };
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Reservation save(Reservation newReservation) {
        String query = "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (:memberId, :date, :time_id, :theme_id)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", newReservation.getMemberId())
                .addValue("date", newReservation.getDate())
                .addValue("time_id", newReservation.getTimeId())
                .addValue("theme_id", newReservation.getThemeId());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, params, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new Reservation(
                id,
                newReservation.getMemberId(),
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
                    r.member_id, 
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
    public List<Reservation> findAllByFilter(ReservationWithFilterRequest filterRequest) {
        String selectQuery =
                """
                SELECT
                    r.id,
                    r.member_id,
                    r.date,
                    rt.start_at,
                    r.theme_id,
                    rt.id as time_id
                FROM reservation as r
                INNER JOIN reservation_time as rt
                ON r.time_id = rt.id
                """;
        List<String> filters = new ArrayList<>();
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (filterRequest.memberId() != null) {
            filters.add("member_id = :member_id");
            params.addValue("member_id", filterRequest.memberId());
        }
        if (filterRequest.themeId() != null) {
            filters.add("theme_id = :theme_id");
            params.addValue("theme_id", filterRequest.themeId());
        }
        if (filterRequest.from() != null) {
            filters.add("date >= :from");
            params.addValue("from", filterRequest.from());
        }
        if (filterRequest.to() != null) {
            filters.add("date <= :to");
            params.addValue("to", filterRequest.to());
        }

        if (filters.isEmpty()) {
            return jdbcTemplate.query(selectQuery, ROW_MAPPER);
        }
        String filterQuery = String.join(" AND ", filters);
        String query = selectQuery + " WHERE " + filterQuery;
        return jdbcTemplate.query(query, params, ROW_MAPPER);
    }

    @Override
    public List<Reservation> findAllByTimeId(Long id) {
        String query = """
                SELECT 
                    r.id, 
                    r.member_id, 
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

    @Override
    public Optional<Reservation> findDuplicatedWith(Reservation entity) {
        String query = """
                SELECT
                    r.id,
                    r.member_id,
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
