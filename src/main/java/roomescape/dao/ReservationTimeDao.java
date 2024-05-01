package roomescape.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.dto.AvailableReservationTimeResponse;
import roomescape.dao.mapper.AvailableReservationTimeMapper;
import roomescape.dao.mapper.ReservationTimeRowMapper;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final ReservationTimeRowMapper rowMapper;
    private final AvailableReservationTimeMapper availableReservationTimeMapper;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate, DataSource dataSource, ReservationTimeRowMapper rowMapper,
                              AvailableReservationTimeMapper availableReservationTimeMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = rowMapper;
        this.availableReservationTimeMapper = availableReservationTimeMapper;
    }

    public ReservationTime create(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAtAsString());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return ReservationTime.from(id, reservationTime.getStartAtAsString());
    }

    public boolean isExistByStartAt(String startAt) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation_time WHERE start_at = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, startAt));
    }

    public Optional<ReservationTime> find(Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<ReservationTime> getAll() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<AvailableReservationTimeResponse> getAvailable(ReservationDate date, Long themeId) {
        String sql = """
                SELECT
                t.id AS time_id,
                r.id IS NOT NULL AS booked,
                t.start_at AS start_at
                FROM reservation_time AS t
                LEFT OUTER JOIN reservation AS r
                ON t.id = r.time_id AND r.theme_id = ? AND r.date = ?;
                """;
        return jdbcTemplate.query(sql, availableReservationTimeMapper, themeId, date.asString());


    }

    public void delete(long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
