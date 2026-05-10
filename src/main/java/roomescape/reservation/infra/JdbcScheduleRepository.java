package roomescape.reservation.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcScheduleRepository implements ScheduleRepository {
    private final NamedParameterJdbcTemplate template;
    private final RowMapper<Schedule> scheduleRowMapper = (resultSet, rowNum) -> new Schedule(
            resultSet.getLong("id"),
            resultSet.getDate("date").toLocalDate(),
            resultSet.getLong("time_id"),
            resultSet.getLong("theme_id")
    );

    @Override
    public Schedule save(Schedule schedule) {
        String sql = "INSERT INTO schedule(date, time_id, theme_id) VALUES (:date, :timeId, :themeId)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", schedule.getDate())
                .addValue("timeId", schedule.getTimeId())
                .addValue("themeId", schedule.getThemeId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, params, keyHolder);

        Number id = keyHolder.getKey();
        if (id == null) {
            throw new IllegalStateException("schedule 저장 후 생성된 ID를 반환받지 못했습니다.");
        }

        return new Schedule(
                id.longValue(),
                schedule.getDate(),
                schedule.getTimeId(),
                schedule.getThemeId()
        );
    }

    @Override
    public Optional<Long> findScheduleIdByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId) {
        String sql = "SELECT s.id " +
                "FROM schedule s " +
                "JOIN reservation_time rt ON rt.id = s.time_id " +
                "JOIN theme t ON s.theme_id = t.id " +
                "WHERE s.date = :date AND s.time_id = :timeId AND s.theme_id = :themeId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("timeId", timeId)
                .addValue("themeId", themeId);

        return template.query(sql, params, (resultSet, rowNum) -> resultSet.getLong("id"))
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Schedule> findById(long id) {
        String sql = "SELECT * FROM schedule WHERE id = :id";

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

    @Override
    public List<Schedule> findAll() {
        String sql = "SELECT * FROM schedule";

        MapSqlParameterSource params = new MapSqlParameterSource();
        return template.query(sql, params, scheduleRowMapper);
    }

    @Override
    public int deleteById(long id) {
        String sql = "DELETE FROM schedule WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return template.update(sql, params);
    }

    @Override
    public Optional<Long> findThemeIdById(long scheduleId) {
        String sql = "SELECT theme_id FROM schedule WHERE id = :scheduleId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", scheduleId);

        return template.query(sql, params, (resultSet, rowNum) -> resultSet.getLong("theme_id"))
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Long> findTimeIdById(long scheduleId) {
        String sql = "SELECT time_id FROM schedule WHERE id = :scheduleId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", scheduleId);

        return template.query(sql, params, (resultSet, rowNum) -> resultSet.getLong("time_id"))
                .stream()
                .findFirst();
    }
}
