package roomescape.infrastructure;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.ReservationTimeRepository;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private static final RowMapper<ReservationTime> ROW_MAPPER = (rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime()
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations jdbcInsert;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new ReservationTime(id, reservationTime);
    }

    @Override
    public Optional<ReservationTime> findById(long id) {
        String query = """
                SELECT id, start_at FROM reservation_time
                WHERE id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Set<Long> findReservedTimeIds(long themeId, LocalDate date) {
        String query = """
                SELECT t.id FROM reservation_time AS t
                JOIN reservation AS r
                ON t.id = r.time_id
                WHERE r.theme_id = ? AND r.reservation_date = ?
                """;
        return new HashSet<>(jdbcTemplate.queryForList(query, Long.class, themeId, date));
    }

    @Override
    public List<ReservationTime> findAll() {
        String query = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    @Override
    public boolean deleteById(long id) {
        String query = "DELETE FROM reservation_time WHERE id = ?";
        int deletedRowCount = jdbcTemplate.update(query, id);
        return deletedRowCount > 0;
    }
}
