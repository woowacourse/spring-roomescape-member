package roomescape.repository.reservationtime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.exceptions.NotDeleteableException;

@Repository
public class ReservationTimeH2Repository implements ReservationTimeRepository {

    private static final String TABLE_NAME = "RESERVATION_TIME";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationTimeH2Repository(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new ReservationTime(id, reservationTime.getStartAt());
    }

    @Override
    public boolean isDuplicatedTime(LocalTime localTime) {
        String sql = "SELECT * FROM reservation_time WHERE start_at = ?";
        return !jdbcTemplate.query(sql, (rs, rowNum) -> 0, localTime).isEmpty();
    }

    @Override
    public void delete(Long id) {
        try {
            jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
        } catch (DataIntegrityViolationException e) {
            throw new NotDeleteableException("예약이 존재하는 시간은 삭제할 수 없습니다. id = " + id);
        }
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        try {
            ReservationTime reservationTime = jdbcTemplate.queryForObject(
                    "SELECT * FROM reservation_time WHERE id = ?",
                    getReservationTimeRowMapper(),
                    id
            );
            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<ReservationTime> getReservationTimeRowMapper() {
        return (resultSet, rowNum) -> new ReservationTime(
                resultSet.getLong("id"),
                LocalTime.parse(resultSet.getString("start_at"))
        );
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM reservation_time",
                getReservationTimeRowMapper()
        );
    }

    @Override
    public Map<ReservationTime, Boolean> findAllWithAlreadyBooked(LocalDate date, Long themeId) {
        String sql = "SELECT rt.id AS time_id, rt.start_at, " +
                "CASE WHEN r.time_id IS NOT NULL THEN true ELSE false END AS already_booked " +
                "FROM reservation_time rt " +
                "LEFT JOIN reservation r ON rt.id = r.time_id AND r.date = ? AND r.theme_id = ? ";

        return jdbcTemplate.queryForObject(
                sql,
                getReservationTimeWithAlreadyBookedRowMapper(),
                date,
                themeId
        );
    }

    private RowMapper<Map<ReservationTime, Boolean>> getReservationTimeWithAlreadyBookedRowMapper() {
        return (resultSet, rowNum) -> {
            Map<ReservationTime, Boolean> result = new LinkedHashMap<>();
            do {
                ReservationTime reservationTime = new ReservationTime(
                        resultSet.getLong("time_id"),
                        LocalTime.parse(resultSet.getString("start_at"))
                );
                Boolean alreadyBooked = resultSet.getBoolean("already_booked");
                result.put(reservationTime, alreadyBooked);
            } while (resultSet.next());
            return result;
        };
    }
}
