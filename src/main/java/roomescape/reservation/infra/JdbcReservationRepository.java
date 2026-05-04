package roomescape.reservation.infra;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Repository
@RequiredArgsConstructor
public class JdbcReservationRepository implements ReservationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> {
        Theme theme = Theme.builder()
                .id(resultSet.getLong("t_id"))
                .name(resultSet.getString("t_name"))
                .thumbnailImageUrl(resultSet.getString("t_thumbnail_image_url"))
                .description(resultSet.getString("t_description"))
                .durationTime(resultSet.getTime("t_duration_time").toLocalTime())
                .build();

        ReservationTime time = ReservationTime.builder()
                .id(resultSet.getLong("rt_id"))
                .startAt(resultSet.getTime("rt_start_at").toLocalTime())
                .build();

        return Reservation.builder()
                .id(resultSet.getLong("r_id"))
                .name(resultSet.getString("r_name"))
                .date(resultSet.getDate("r_date").toLocalDate())
                .time(time)
                .theme(theme)
                .build();
    };


    @Override
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation(name, date, time_id, theme_id) "
                + "VALUES(:name, :date, :timeId, :themeId)";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("timeId", reservation.getTime().getId())
                .addValue("themeId", reservation.getTheme().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, params, keyHolder);
        long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return reservation.withId(generatedId);
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT "
                + "r.id AS r_id, r.name AS r_name, r.date AS r_date, "
                + "t.id AS t_id, t.name AS t_name, t.thumbnail_image_url AS t_thumbnail_image_url, "
                + "t.description AS t_description, t.duration_time AS t_duration_time, "
                + "rt.id AS rt_id, rt.start_at AS rt_start_at "
                + "FROM reservation r "
                + "INNER JOIN theme t ON r.theme_id = t.id "
                + "INNER JOIN reservation_time rt ON r.time_id = rt.id "
                + "ORDER BY r.date ASC, rt.start_at ASC";

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Reservation> findByThemeAndDate(Long themeId, LocalDate date) {
        String sql = "SELECT "
                + "r.id AS r_id, r.name AS r_name, r.date AS r_date, "
                + "t.id AS t_id, t.name AS t_name, t.thumbnail_image_url AS t_thumbnail_image_url, "
                + "t.description AS t_description, t.duration_time AS t_duration_time, "
                + "rt.id AS rt_id, rt.start_at AS rt_start_at "
                + "FROM reservation r "
                + "INNER JOIN theme t ON r.theme_id = t.id "
                + "INNER JOIN reservation_time rt ON r.time_id = rt.id "
                + "WHERE r.theme_id = :themeId AND r.date = :date";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("themeId", themeId)
                .addValue("date", date);

        return jdbcTemplate.query(sql, params, rowMapper);
    }

    @Override
    public boolean existsByReservationTime(Long timeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id=:timeId)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Map.of("timeId", timeId), Boolean.class));
    }

    @Override
    public boolean existsByReservationTimeAndThemeAndDate(Long timeId, Long themeId, LocalDate date) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id=:timeId AND theme_id=:themeId AND date=:date)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Map.of("timeId", timeId, "themeId", themeId, "date", date), Boolean.class));
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id=:id";
        return jdbcTemplate.update(sql, Map.of("id", id));
    }
}
