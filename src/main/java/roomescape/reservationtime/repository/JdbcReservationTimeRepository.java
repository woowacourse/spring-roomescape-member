package roomescape.reservationtime.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.domain.ReservationTime;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return jdbcTemplate.query(
                "SELECT id, start_at FROM reservation_time WHERE id = ?",
                (rs, rowNum) -> ReservationTime.builder()
                        .id(rs.getLong("id"))
                        .startAt(rs.getTime("start_at").toLocalTime())
                        .build(),
                id).stream().findFirst();
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query(
                "SELECT id, start_at FROM reservation_time",
                (rs, rowNum) -> ReservationTime.builder()
                        .id(rs.getLong("id"))
                        .startAt(rs.getTime("start_at").toLocalTime())
                        .build()
        );
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return reservationTime.withId(id);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
    }
}
