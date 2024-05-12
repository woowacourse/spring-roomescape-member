package roomescape.repository;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.ReservationTime;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationTimeDao {
    private static final String TABLE_NAME = "reservation_time";
    private static final String KEY_COLUMN_NAME = "id";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<ReservationTime> reservationTimeRowMapper;

    public ReservationTimeDao(
            final JdbcTemplate jdbcTemplate,
            final DataSource source,
            final RowMapper<ReservationTime> reservationTimeRowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(source)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
        this.reservationTimeRowMapper = reservationTimeRowMapper;
    }

    public ReservationTime save(final ReservationTime reservationTime) {
        try {
            SqlParameterSource params = makeInsertParams(reservationTime);
            long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
            return makeSavedTime(reservationTime, id);
        } catch (DuplicateKeyException exception) {
            throw new IllegalStateException("[ERROR] 키 값 에러 : 중복된 시간 키가 존재합니다");
        }
    }

    public List<ReservationTime> getAll() {
        return jdbcTemplate.query(
                "SELECT id as time_id, start_at FROM reservation_time",
                reservationTimeRowMapper
        );
    }

    public Optional<ReservationTime> findById(final long id) {
        try {
            String sql = "SELECT id as time_id, * FROM reservation_time WHERE id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void delete(final long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", Long.valueOf(id));
    }

    private MapSqlParameterSource makeInsertParams(ReservationTime reservationTime) {
        return new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt().toString());
    }

    private ReservationTime makeSavedTime(ReservationTime reservationTime, long id) {
        return new ReservationTime(id, reservationTime.getStartAt());
    }
}
