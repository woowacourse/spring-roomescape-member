package roomescape.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

@Repository
public class ReservationDaoImpl implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert insertActor;

    public ReservationDaoImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> getAllReservations() {
        String sql = """
                SELECT 
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description,
                    th.thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
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
    public Reservation addReservation(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("name", reservation.getName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getThemeId());
        Number newId = insertActor.executeAndReturnKey(parameters);
        return new Reservation(newId.longValue(), reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    @Override
    public void deleteReservation(long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Long countReservationById(long id) {
        String sql = "SELECT count(id) FROM reservation WHERE id = ?";
        Long countReservation = jdbcTemplate.queryForObject(sql, (resultSet, ignored) -> resultSet.getLong(1), id);
        validateNullResult(countReservation); //todo 예외는 꼭 예외를 발생시켜야 할 때만 해야함 : 그냥 0으로 반환하면 안되나? count잖아!!
        return countReservation;
    }

    @Override
    public Long countReservationByTimeId(long timeId) {
        String sql = "SELECT count(id) FROM reservation WHERE time_id = ?";
        Long countReservation = jdbcTemplate.queryForObject(sql, (resultSet, ignored) -> resultSet.getLong(1), timeId);
        validateNullResult(countReservation);
        return countReservation;
    }

    @Override
    public Long countReservationByDateAndTimeId(LocalDate date, long timeId) {
        String sql = "SELECT count(id) FROM reservation WHERE date = ? AND time_id = ?";
        Long countReservation = jdbcTemplate.queryForObject(sql, (resultSet, ignored) -> resultSet.getLong(1), date,
                timeId);
        validateNullResult(countReservation);
        return countReservation;
    }

    @Override
    public List<ReservationTime> findReservationTimeByDateAndTheme(LocalDate date, long themeId) {
        String sql = """
                SELECT t.id AS time_id, t.start_at AS start_at
                FROM reservation AS r INNER JOIN reservation_time AS t ON r.time_id = t.id
                WHERE date = ? AND theme_id = ?
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("start_at").toLocalTime()
                ), date, themeId);
    }

    private void validateNullResult(Long queryResult) {
        if (queryResult == null) {
            throw new NullPointerException("쿼리 실행 결과가 null 입니다.");
        }
    }
}
