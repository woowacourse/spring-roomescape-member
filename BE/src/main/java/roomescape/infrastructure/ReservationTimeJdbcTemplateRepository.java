package roomescape.infrastructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.entity.ReservationTime;
import roomescape.entity.ReservationTimeRepository;

@Repository
public class ReservationTimeJdbcTemplateRepository implements ReservationTimeRepository {

    private static final String FIND_BY_QUERY = "SELECT id, start_at FROM reservation_time WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, start_at FROM reservation_time";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM reservation_time WHERE id = ?";

    private static final RowMapper<ReservationTime> ROW_MAPPER = (rs, rowNum) -> ReservationTime.createWithId(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime()
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeJdbcTemplateRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public ReservationTime save(ReservationTime reservation) {
        Long id = simpleJdbcInsert.executeAndReturnKey(prepareInsertParameters(reservation)).longValue();
        return ReservationTime.createWithId(id, reservation.startAt());
    }

    private Map<String, Object> prepareInsertParameters(ReservationTime reservation) {
        return Map.of("start_at", reservation.startAt());
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        List<ReservationTime> reservationTime = jdbcTemplate.query(FIND_BY_QUERY, ROW_MAPPER, id);
        return reservationTime.stream()
                .findFirst();
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, ROW_MAPPER);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
    }
}
