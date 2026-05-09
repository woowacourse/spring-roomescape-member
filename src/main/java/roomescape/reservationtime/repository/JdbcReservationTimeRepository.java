package roomescape.reservationtime.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    public static final RowMapper<ReservationTime> RESERVATION_TIME_ROW_MAPPER = (rs, rowNum) ->
            ReservationTime.of(
                    rs.getLong("id"),
                    rs.getObject("start_at", LocalTime.class)
            );

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName("RESERVATION_TIME")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return ReservationTime.toEntity(reservationTime, id);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
        try {
            ReservationTime reservationTime = jdbcTemplate.queryForObject(sql, RESERVATION_TIME_ROW_MAPPER, id);
            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time ORDER BY id";
        return jdbcTemplate.query(sql, RESERVATION_TIME_ROW_MAPPER);
    }

    @Override
    public List<ReservationTime> findAvailableReservationTimes(LocalDate date, Long themeId) {
        String sql = """
                SELECT
                    rt.id,
                    rt.start_at
                FROM reservation_time rt
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM reservation r
                    WHERE r.time_id = rt.id
                        AND r.date = ?
                        AND r.theme_id = ?
                )
                ORDER BY rt.id
                """;
        return jdbcTemplate.query(sql, RESERVATION_TIME_ROW_MAPPER, date, themeId);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";

        int affectedRows = jdbcTemplate.update(sql, id);
        if (affectedRows == 0) {
            throw new ReservationTimeNotFoundException(id);
        }
    }

}
