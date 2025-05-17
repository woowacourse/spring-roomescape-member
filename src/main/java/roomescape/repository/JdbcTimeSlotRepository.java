package roomescape.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.TimeSlot;
import roomescape.repository.support.DomainMapper;

@Repository
public class JdbcTimeSlotRepository implements TimeSlotRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcTimeSlotRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION_TIME")
                .usingGeneratedKeyColumns("ID");
    }

    @Override
    public List<TimeSlot> findAll() {
        final String sql = "SELECT * FROM RESERVATION_TIME ORDER BY START_AT";

        return jdbcTemplate.query(sql, DomainMapper.TIMESLOT);
    }

    @Override
    public Long save(final TimeSlot timeSlot) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("ID", timeSlot.id())
                .addValue("START_AT", timeSlot.startAt());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public Optional<TimeSlot> findById(final Long id) {
        final String sql = "SELECT * FROM RESERVATION_TIME WHERE ID = ?";
        final List<TimeSlot> timeSlots = jdbcTemplate.query(sql, DomainMapper.TIMESLOT, id);

        return timeSlots.stream().findAny();
    }

    @Override
    public Boolean removeById(final Long id) {
        final String sql = "DELETE FROM RESERVATION_TIME WHERE ID = ?";
        final int removedRowsCount = jdbcTemplate.update(sql, id);

        return removedRowsCount > 0;
    }
}
