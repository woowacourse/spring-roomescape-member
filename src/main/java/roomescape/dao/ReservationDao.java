package roomescape.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("reservation")
            .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(
            """
                SELECT
                r.id as reservation_id,
                r.name,
                r.date,
                rt.id as time_id,
                rt.start_at as time_value,
                t.id as theme_id,
                t.name as theme_name,
                t.description as theme_description,
                t.thumbnail as theme_thumbnail
                FROM reservation as r
                inner join reservation_time as rt
                on r.time_id = rt.id
                inner join theme as t
                on r.theme_id = t.id
                """,
            (resultSet, rowNum) ->
            {
                LocalDate date = LocalDate.parse(
                    resultSet.getString("date"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                );
                return new Reservation(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("name"),
                    date,
                    new ReservationTime(
                        resultSet.getLong("time_id"),
                        LocalTime.parse(
                            resultSet.getString("time_value"),
                            DateTimeFormatter.ofPattern("HH:mm")
                        )
                    ),
                    new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("theme_description"),
                        resultSet.getString("theme_thumbnail")
                    )
                );
            }
        );
    }

    public Reservation save(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", reservation.getName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getId());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new Reservation(
            id,
            reservation.getName(),
            reservation.getDate(),
            reservation.getTime(),
            reservation.getTheme()
        );
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("delete from reservation where id = ?", id);
    }

    public int getCountByThemeId(Long themeId) {
        String query = "SELECT count(*) FROM reservation WHERE theme_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, themeId);
    }

    public int getCountByTimeIdAndThemeIdAndDate(Long timeId, Long themeId, LocalDate date) {
        String query = "SELECT count(*) FROM reservation WHERE time_id = ? and theme_id = ? and date = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, timeId, themeId, date);
    }
}
