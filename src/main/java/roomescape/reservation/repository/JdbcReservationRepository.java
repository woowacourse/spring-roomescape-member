package roomescape.reservation.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.Reservation;
import roomescape.reservation.repository.projection.ReservationDetailProjection;
import roomescape.reservationtime.dto.response.TimeInformation;
import roomescape.theme.dto.response.ThemeFindResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcReservationRepository implements ReservationRepository {
    private final NamedParameterJdbcTemplate template;
    private final RowMapper<ReservationDetailProjection> reservationDetailFindRowMapper = (resultSet, rowNum) ->
            new ReservationDetailProjection(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("reservation_name"),
                    resultSet.getDate("date").toLocalDate(),
                    new ThemeFindResponse(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("theme_description"),
                            resultSet.getString("theme_thumbnail_url")
                    ),
                    new TimeInformation(
                            resultSet.getLong("time_id"),
                            resultSet.getTime("start_at").toLocalTime()
                    )
            );

    @Override
    public Reservation save(Reservation reservation) {
        String insertReservationSql = "INSERT INTO reservation(name, schedule_id) VALUES (:name, :scheduleId)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("scheduleId", reservation.getScheduleId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(insertReservationSql, params, keyHolder);

        Number id = keyHolder.getKey();
        if (id == null) {
            throw new IllegalStateException("reservation 저장 후 생성된 ID를 반환받지 못했습니다.");
        }

        return new Reservation(
                keyHolder.getKey().longValue(),
                reservation.getName(),
                reservation.getScheduleId()
        );
    }

    @Override
    public List<ReservationDetailProjection> findAllDetails() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name AS reservation_name,
                    s.date,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description AS theme_description,
                    t.thumbnail_url AS theme_thumbnail_url,
                    rt.id AS time_id,
                    rt.start_at
                FROM reservation r
                JOIN schedule s ON r.schedule_id = s.id
                JOIN theme t ON s.theme_id = t.id
                JOIN reservation_time rt ON s.time_id = rt.id
                ORDER BY r.id
                """;

        return template.query(sql, reservationDetailFindRowMapper);
    }

    @Override
    public int deleteById(long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        return template.update(sql, params);
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

    @Override
    public List<ReservationDetailProjection> findDetailsByName(String name) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name AS reservation_name,
                    s.date,
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description AS theme_description,
                    t.thumbnail_url AS theme_thumbnail_url,
                    rt.id AS time_id,
                    rt.start_at
                FROM reservation r
                JOIN schedule s ON r.schedule_id = s.id
                JOIN theme t ON s.theme_id = t.id
                JOIN reservation_time rt ON s.time_id = rt.id
                WHERE r.name = :name
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", name);

        return template.query(sql, params, reservationDetailFindRowMapper);
    }

    @Override
    public int deleteByIdAndName(long id, String name) {
        String sql = "DELETE FROM reservation WHERE id = :id AND name = :name";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("name", name);

        return template.update(sql, params);
    }
}
