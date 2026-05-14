package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationStatus;
import roomescape.domain.ReservationTime;
import roomescape.domain.TimeStatus;

@Repository
@RequiredArgsConstructor
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ReservationTime> rowMapper = (rs, rowNum) -> {
        ReservationTime reservationTime = ReservationTime.of(
                rs.getLong("id"),
                rs.getObject("start_at", LocalTime.class)
        );

        if(TimeStatus.DELETED.name().equals(rs.getString("status"))) {
            return reservationTime.deleted();
        }

        return reservationTime;
    };

    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.startAt())
                .addValue("status", TimeStatus.AVAILABLE.name());

        SimpleJdbcInsert insertExecutor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");

        Number newId = insertExecutor.executeAndReturnKey(params);

        return ReservationTime.of(
                newId.longValue(),
                reservationTime.startAt()
        );
    }

    public void deleteByTimeId(long timeId) {
        String sql = "UPDATE reservation_time SET status = ? WHERE id = ?";
        int affected = jdbcTemplate.update(sql, TimeStatus.DELETED.name(),timeId);

        if (affected == 0) {
            throw new NoSuchElementException("[ERROR] 삭제할 id에 해당하는 시간이 존재하지 않습니다.");
        }
    }

    public List<ReservationTime> findAllReservationTimes() {
        String sql = "SELECT id, start_at, status FROM reservation_time WHERE status = ?";
        return jdbcTemplate.query(sql, rowMapper, TimeStatus.AVAILABLE.name());
    }

    public List<ReservationTime> findAvailableReservationTimes(LocalDate date, long themeId) {
        String sql = """
                SELECT 
                    rt.id, 
                    rt.start_at,
                    rt.status
                FROM reservation_time rt
                LEFT JOIN reservation r
                    ON rt.id = r.time_id
                    AND r.date = ?
                    AND r.theme_id = ?
                    AND r.status = ?
                WHERE r.id IS NULL
                    AND rt.status = ?
                """;

        return jdbcTemplate.query(sql, rowMapper, date, themeId, ReservationStatus.AVAILABLE.name(), TimeStatus.AVAILABLE.name());
    }

    public Optional<ReservationTime> findByTimeId(long timeId) {
        String sql = "SELECT id, start_at, status FROM reservation_time WHERE id = ? AND status = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, timeId, TimeStatus.AVAILABLE.name()));
    }
}
