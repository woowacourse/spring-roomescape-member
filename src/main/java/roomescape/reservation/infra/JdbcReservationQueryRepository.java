package roomescape.reservation.infra;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.repository.ReservationQueryRepository;

@Repository
public class JdbcReservationQueryRepository implements ReservationQueryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationQueryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM reservation WHRER time_id = ?"
                , Integer.class
                , timeId);
        return result > 0;
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM reservation WHRER time_id = ?"
                , Integer.class
                , themeId);
        return result > 0;
    }
}
