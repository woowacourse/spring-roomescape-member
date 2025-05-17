package roomescape.persistence.jdbc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.persistence.entity.ReservationTimeEntity;
import roomescape.presentation.member.dto.AvailableTimesResponseDto;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAll() {
        String query = """
                SELECT id, start_at 
                FROM reservation_time
                """;
        List<ReservationTimeEntity> timeEntities = jdbcTemplate.query(
                query,
                (rs, rowNum) -> new ReservationTimeEntity(
                        rs.getLong("id"),
                        rs.getString("start_at")
                )
        );
        return timeEntities.stream()
                .map(ReservationTimeEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String query = """
                SELECT id, start_at 
                FROM reservation_time 
                WHERE id = ?
                """;
        return jdbcTemplate.query(query, (rs, rowNum) -> new ReservationTimeEntity(
                                rs.getLong("id"),
                                rs.getString("start_at")
                        ),
                        id
                )
                .stream()
                .findFirst()
                .map(ReservationTimeEntity::toDomain);
    }

    @Override
    public ReservationTime add(ReservationTime reservationTime) {
        ReservationTimeEntity reservationTimeEntity = ReservationTimeEntity.fromDomain(reservationTime);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start_at", reservationTimeEntity.getStartAt());
        long id = jdbcInsert.executeAndReturnKey(parameters).longValue();
        return reservationTimeEntity.copyWithId(id).toDomain();
    }

    @Override
    public void deleteById(Long id) {
        String query = """
                DELETE FROM reservation_time
                WHERE id = ?
                """;
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<AvailableTimesResponseDto> findAvailableTimes(LocalDate date, Long themeId) {
        String query = """
                SELECT t.id, t.start_at, r.id AS reservation_id
                FROM reservation_time t
                LEFT JOIN reservation r
                    ON t.id = r.time_id
                   AND r.date = ?
                   AND r.theme_id = ?
                """;
        return jdbcTemplate.query(query, (rs, rowNum) -> new AvailableTimesResponseDto(
                        rs.getLong("id"),
                        rs.getObject("start_at", LocalTime.class),
                        rs.getObject("reservation_id") != null
                ),
                date,
                themeId
        );
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        String query = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation_time
                    WHERE start_at = ?
                )
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, startAt));
    }
}
