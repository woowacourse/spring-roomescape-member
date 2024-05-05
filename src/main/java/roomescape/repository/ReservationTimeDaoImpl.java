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
        String sql = "select id, start_at from reservation_time";
        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
                new ReservationTime(
                        resultSet.getLong("id"),
                        resultSet.getTime("start_at").toLocalTime()
                ));
    }

    @Override
    public List<ReservationTime> findAllReservedTimes(LocalDate date, long themeId) {
        String sql = """
                select t.id as time_id, t.start_at as start_at
                from reservation as r inner join reservation_time as t on r.time_id = t.id
                where date = ? and theme_id = ?
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("start_at").toLocalTime()
                ), date, themeId);
    }

    @Override
    public ReservationTime findReservationById(long id) {
        String sql = "select * from reservation_time where id = ?";
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
        String sql = "delete from reservation_time where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Long countReservationTimeById(long id) {
        String sql = "select count(id) from reservation_time where id = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, ignored) ->
                resultSet.getLong(1), id);
    }

    @Override
    public Long countReservationTimeByStartAt(LocalTime startAt) {
        String sql = "select count(id) from reservation_time where start_at = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, ignored) ->
                resultSet.getLong(1), startAt);
    }
}
