package roomescape.reservation.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Schedule;
import roomescape.reservation.domain.Theme;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcScheduleRepository implements ScheduleRepository {
    private final NamedParameterJdbcTemplate template;
    private final RowMapper<Schedule> scheduleRowMapper = (resultSet, rowNum) -> new Schedule(
            resultSet.getLong("id"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("start_at").toLocalTime()
            ),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail_url")
            )
    );

    @Override
    public Optional<Schedule> findByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId) {
        String sql = "SELECT * " +
                "FROM schedule s " +
                "JOIN reservation_time rt ON rt.id = s.time_id " +
                "JOIN theme t ON s.theme_id = t.id " +
                "WHERE s.date = :date AND s.time_id = :timeId AND s.theme_id = :themeId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("timeId", timeId)
                .addValue("themeId", themeId);

        return template.query(sql, params, scheduleRowMapper).stream().findFirst();
    }

    @Override
    public Optional<Schedule> findById(long id) {
        String sql = "SELECT * " +
                "FROM schedule s " +
                "JOIN reservation_time rt ON rt.id = s.time_id " +
                "JOIN theme t ON s.theme_id = t.id " +
                "WHERE s.id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return template.query(sql, params, scheduleRowMapper).stream().findFirst();
    }

    @Override
    public boolean existsByTimeId(long timeId) {
        String sql = "SELECT COUNT(1) FROM schedule WHERE time_id = :timeId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("timeId", timeId);

        Integer count = template.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByThemeId(long themeId) {
        String sql = "SELECT COUNT(1) FROM schedule WHERE theme_id = :themeId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("themeId", themeId);

        Integer count = template.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
}
