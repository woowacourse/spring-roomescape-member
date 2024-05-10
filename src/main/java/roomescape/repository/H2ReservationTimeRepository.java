package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class H2ReservationTimeRepository implements ReservationTimeRepository {
    private static final String ID = "id";
    private static final String START_AT = "start_at";

    private final ReservationTimeRowMapper rowMapper;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public H2ReservationTimeRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.rowMapper = new ReservationTimeRowMapper();
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns(ID);
    }

    public ReservationTime save(ReservationTime reservationTime) {
        Long reservationTimeId = jdbcInsert.executeAndReturnKey(Map.of(
                        START_AT, reservationTime.getStartAt()
        )).longValue();

        return new ReservationTime(
                reservationTimeId,
                reservationTime.getStartAt()
        );
    }

    public List<ReservationTime> findAll() {
        String sql = "select * from reservation_time";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<ReservationTime> findById(Long id) {
        String sql = "select * from reservation_time where id = ?";
        List<ReservationTime> reservationTimes = jdbcTemplate.query(sql, rowMapper, id);

        return reservationTimes.stream().findFirst();
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation_time where id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static class ReservationTimeRowMapper implements RowMapper<ReservationTime> {
        @Override
        public ReservationTime mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ReservationTime(
                    rs.getLong(ID),
                    rs.getTime(START_AT).toLocalTime()
            );
        }
    }
}
