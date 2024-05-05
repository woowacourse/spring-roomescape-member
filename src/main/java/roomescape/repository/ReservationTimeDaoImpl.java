package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.model.ReservationTime;

@Repository
public class ReservationTimeDaoImpl implements ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert insertActor;

    public ReservationTimeDaoImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAllReservationTimes() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
                new ReservationTime(
                        resultSet.getLong("id"),
                        resultSet.getTime("start_at").toLocalTime()
                ));
    }

    @Override
    public List<ReservationTime> findAllReservedTimes(LocalDate date, long themeId) {
        String sql = """
                SELECT t.id AS time_id, t.start_at AS start_at
                FROM reservation AS r INNER JOIN reservation_time AS t ON r.time_id = t.id
                WHERE DATE = ? AND theme_id = ?
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("start_at").toLocalTime()
                ), date, themeId);
    }

    @Override
    public ReservationTime findReservationById(long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) ->
                new ReservationTime(
                        resultSet.getLong("id"),
                        resultSet.getTime("start_at").toLocalTime()
                ), id);
    }

    @Override
    public ReservationTime addReservationTime(ReservationTime reservationTime) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("start_at", reservationTime.getStartAt());
        Number newId = insertActor.executeAndReturnKey(parameters);
        return new ReservationTime(newId.longValue(), reservationTime.getStartAt());
    }

    @Override
    public void deleteReservationTime(long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Long countReservationTimeById(long id) {
        String sql = "SELECT count(id) FROM reservation_time WHERE id = ?";
        Long countReservationTime =
                jdbcTemplate.queryForObject(sql, (resultSet, ignored) -> resultSet.getLong(1), id);
        validateNullResult(countReservationTime);
        return countReservationTime;
    }

    @Override
    public Long countReservationTimeByStartAt(LocalTime startAt) {
        String sql = "SELECT count(id) FROM reservation_time WHERE start_at = ?";
        Long countReservationTime =
                jdbcTemplate.queryForObject(sql, (resultSet, ignored) -> resultSet.getLong(1), startAt);
        validateNullResult(countReservationTime);
        return countReservationTime;
    }

    private void validateNullResult(Long queryResult) {
        if (queryResult == null) {
            throw new NullPointerException("쿼리 실행 결과가 null 입니다.");
        }
    }
}
