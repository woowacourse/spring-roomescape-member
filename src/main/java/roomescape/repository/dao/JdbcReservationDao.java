package roomescape.repository.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert insertActor;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAllReservations() {
        String sql = """
                select
                    r.id as reservation_id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_start_at,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description,
                    th.thumbnail
                from reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
                new Reservation(
                        resultSet.getLong("reservation_id"),
                        resultSet.getString("name"),
                        resultSet.getDate("date").toLocalDate(),
                        new ReservationTime(
                                resultSet.getLong("time_id"),
                                resultSet.getTime("time_start_at").toLocalTime()
                        ),
                        new Theme(
                                resultSet.getLong("theme_id"),
                                resultSet.getString("theme_name"),
                                resultSet.getString("description"),
                                resultSet.getString("thumbnail")
                        )
                ));
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", reservation.getName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getThemeId());
        Number newId = insertActor.executeAndReturnKey(parameters);
        return new Reservation(newId.longValue(), reservation.getName(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    @Override
    public void deleteReservationById(long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Long countReservationById(long id) {
        String sql = "select count(id) from reservation where id = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, ignored) -> resultSet.getLong(1), id);
    }

    @Override
    public Long countReservationByTimeId(long timeId) {
        String sql = "select count(id) from reservation where time_id = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, ignored) -> resultSet.getLong(1), timeId);
    }

    @Override
    public Long countReservationByDateAndTimeId(LocalDate date, long timeId) {
        String sql = "select count(id) from reservation where date = ? and time_id = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, ignored) -> resultSet.getLong(1), date, timeId);
    }

    @Override
    public List<ReservationTime> findReservationTimeByDateAndThemeId(LocalDate date, long themeId) {
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
}
