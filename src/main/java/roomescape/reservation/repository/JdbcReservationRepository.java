package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.ReservationTime;

@Repository
@RequiredArgsConstructor
public class JdbcReservationRepository implements ReservationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> {
        Long id = resultSet.getLong("reservation_id");
        String name = resultSet.getString("name");
        LocalDate date = resultSet.getObject("date", LocalDate.class);
        Long timeId = resultSet.getLong("time_id");
        LocalTime timeValue = resultSet.getObject("time_value", LocalTime.class);
        ReservationTime timeEntity = new ReservationTime(timeId, timeValue);
        Long themeId = resultSet.getLong("theme_id");

        return new Reservation(
                id,
                name,
                date,
                timeEntity,
                themeId
        );
    };

    @Override
    public Reservation save(Reservation reservation) {
        String sql = """
                INSERT INTO reservation (name, date, time_id, theme_id) 
                VALUES (:name, :date, :time_id, :theme_id)
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getThemeId());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);

        return new Reservation(
                keyHolder.getKey().longValue(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getThemeId()
        );
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT 
                    r.id as reservation_id, 
                    r.name, 
                    r.date, 
                    t.id as time_id, 
                    t.start_at as time_value, 
                    r.theme_id 
                FROM reservation r 
                INNER JOIN reservation_time t ON r.time_id = t.id
                ORDER BY r.date, t.start_at
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Reservation> findAllByTimeId(Long id) {
        String sql = """
                SELECT 
                    r.id, 
                    r.name, 
                    r.date, 
                    rt.start_at,
                    r.theme_id
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                WHERE rt.id = :id
                ORDER BY r.date
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(sql, params, rowMapper);
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        int updated = jdbcTemplate.update(sql, params);
        return updated > 0;
    }

    @Override
    public boolean existsByDateAndTimeId(LocalDate date, Long timeId) {
        String sql = """
                SELECT COUNT(*)
                FROM reservation
                WHERE date = :date
                AND time_id = :timeId
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("timeId", timeId);

        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
}
