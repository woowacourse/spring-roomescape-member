package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll(int page, int size) {
        String sql = """
                 SELECT 
                     r.id as reservation_id, 
                     r.name as member_name, 
                     r.date, 
                     rt.id as time_id, 
                     rt.start_at as time_value, 
                     th.id as theme_id, 
                     th.name as theme_name,
                     th.description,
                     th.thumbnail
                 FROM reservation r 
                 INNER JOIN reservation_time rt 
                 ON r.time_id = rt.id 
                 INNER JOIN theme th 
                 ON r.theme_id = th.id 
                 ORDER BY r.date ASC, rt.start_at ASC, th.name ASC, r.name ASC 
                 LIMIT ? OFFSET ? 
                """;

        int offset = page * size;

        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> new Reservation(
                        resultSet.getLong("reservation_id"),
                        resultSet.getString("member_name"),
                        LocalDate.parse(resultSet.getString("date")),
                        new ReservationTime(
                                resultSet.getLong("time_id"),
                                LocalTime.parse(resultSet.getString("time_value"))),
                        new Theme(
                                resultSet.getLong("theme_id"),
                                resultSet.getString("theme_name"),
                                resultSet.getString("description"),
                                resultSet.getString(("thumbnail"))
                        )

                ),
                size,
                offset);
    }

    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES(?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});

            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setString(2, reservation.getDate().toString());
            preparedStatement.setLong(3, reservation.getTimeId());
            preparedStatement.setLong(4, reservation.getThemeId());

            return preparedStatement;
        }, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    public List<Long> findReservedTimeIdsByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                select time_id 
                from reservation 
                where date = ? 
                and theme_id = ?
                """;

        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> resultSet.getLong("time_id"),
                date.toString(),
                themeId
        );
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existByTimeId(Long timeId) {
        String sql = "SELECT count(*) FROM reservation where time_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, timeId);
        return count != null && count > 0;
    }

    public boolean existByThemeId(Long themeId) {
        String sql = "SELECT count(*) FROM reservation where theme_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, themeId);
        return count != null && count > 0;
    }

    public boolean existById(Long id) {
        String sql = "SELECT count(*) FROM reservation where id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public boolean existByDateAndTimeAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = """
                SELECT count(*) FROM reservation
                where date = ?
                and time_id = ?
                and theme_id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, date, timeId, themeId);
        return count != null && count > 0;
    }
}
