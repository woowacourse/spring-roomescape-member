package roomescape.repository.reservationtime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
import roomescape.exceptions.UserException;

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
        // TODO: 서비스 단에서 예외처리
        if (isDuplicatedTime(reservationTime.getStartAt())) {
            throw new UserException("이미 존재하는 시간입니다.");
        }

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new ReservationTime(id, reservationTime.getStartAt());
    }

    private boolean isDuplicatedTime(LocalTime localTime) {
        String sql = "SELECT * FROM reservation_time WHERE start_at = ?";
        return !jdbcTemplate.query(sql, (rs, rowNum) -> 0, localTime).isEmpty();
    }

    @Override
    public void delete(Long id) {
        try {
            jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
        } catch (DataIntegrityViolationException e) {
            throw new UserException("참조되고 있는 시간을 삭제할 수 없습니다. id = " + id);
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
    public List<ReservationTime> findAllWithAlreadyBooked(LocalDate date, Long themeId) {
        String sql = "SELECT rt.id AS time_id, rt.start_at, " +
                "CASE WHEN r.time_id IS NOT NULL THEN true ELSE false END AS already_booked " +
                "FROM reservation_time rt " +
                "LEFT JOIN reservation r ON rt.id = r.time_id AND r.date = ? AND r.theme_id = ? ";

        return jdbcTemplate.query(sql, getReservationTimeRowMapperWithAlreadyBooked(), date, themeId);
    }

    private RowMapper<ReservationTime> getReservationTimeRowMapperWithAlreadyBooked() {
        return (resultSet, rowNum) -> new ReservationTime(
                resultSet.getLong("id"),
                LocalTime.parse(resultSet.getString("start_at")),
                resultSet.getBoolean("already_booked")
        );
    }
}
