package roomescape.reservation.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Schedule;
import roomescape.reservation.domain.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcReservationRepository implements ReservationRepository {
    private final NamedParameterJdbcTemplate template;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    new Schedule(
                            resultSet.getLong("schedule_id"),
                            resultSet.getDate("date").toLocalDate(),
                            new ReservationTime(
                                    resultSet.getLong("time_id"),
                                    resultSet.getTime("start_at").toLocalTime()
                            ),
                            new Theme(
                                    resultSet.getLong("theme_id"),
                                    resultSet.getString("theme_name"),
                                    resultSet.getString("description"),
                                    resultSet.getString("thumbnail_url")
                            )
                    )
            );

    @Override
    public Reservation save(Reservation reservation) {
        String insertReservationSql = "INSERT INTO reservation(name, schedule_id) VALUES (:name, :scheduleId)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("scheduleId", reservation.getSchedule().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(insertReservationSql, params, keyHolder);

        Number id = keyHolder.getKey();
        if (id == null) {
            throw new IllegalStateException("reservation 저장 후 생성된 ID를 반환받지 못했습니다.");
        }

        return new Reservation(
                keyHolder.getKey().longValue(),
                reservation.getName(),
                new Schedule(
                        reservation.getSchedule().getId(),
                        reservation.getSchedule().getDate(),
                        reservation.getSchedule().getTime(),
                        reservation.getSchedule().getTheme()
                )
        );
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.schedule_id,
                    s.date,
                    s.time_id,
                    rt.start_at,
                    s.theme_id,
                    t.name AS theme_name,
                    t.description,
                    t.thumbnail_url
                FROM reservation r
                JOIN schedule s ON r.schedule_id = s.id
                JOIN reservation_time rt ON s.time_id = rt.id
                JOIN theme t ON s.theme_id = t.id
                """;

        return template.query(sql, reservationRowMapper);
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        template.update(sql, params);
    }

    @Override
    public Set<Long> findTimeIdByDateAndThemeId(LocalDate date, long themeId) {
        String sql = """
                SELECT
                    s.time_id
                FROM schedule s
                LEFT JOIN reservation r ON s.id = r.schedule_id
                WHERE s.date = :date
                AND s.theme_id = :themeId
                AND r.id IS NOT NULL
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("themeId", themeId);

        return Set.copyOf(template.query(sql, params,
                (rs, rowNum) -> rs.getLong("time_id")));
    }
}
