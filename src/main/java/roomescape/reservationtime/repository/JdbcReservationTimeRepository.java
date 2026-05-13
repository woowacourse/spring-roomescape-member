package roomescape.reservationtime.repository;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private static final String RESERVATION_TIME_BASE_SELECT = """
            SELECT rt.id,
                   rt.start_at,
                   t.id AS theme_id,
                   t.name AS theme_name,
                   t.description,
                   t.thumbnail_url
            FROM reservation_time AS rt
            INNER JOIN theme AS t ON rt.theme_id = t.id
            """;

    private static final RowMapper<ReservationTime> reservationTimeRowMapper = (resultSet, rowNum) -> {
        Theme theme = Theme.of(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail_url")
        );

        return ReservationTime.of(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime(),
                theme
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationTimeRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        String sql = "INSERT INTO reservation_time (start_at, theme_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setTime(1, Time.valueOf(reservationTime.getStartAt()));
            preparedStatement.setLong(2, reservationTime.getTheme().getId());
            return preparedStatement;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("[ERROR] 예약 ID를 생성하지 못했습니다.");
        }

        return reservationTime.withId(key.longValue());
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = RESERVATION_TIME_BASE_SELECT + " ORDER BY t.id, rt.start_at";

        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    @Override
    public List<ReservationTime> findAllByThemeId(final long themeId) {
        String sql = RESERVATION_TIME_BASE_SELECT + " WHERE rt.theme_id = ? ORDER BY rt.start_at";

        return jdbcTemplate.query(sql, reservationTimeRowMapper, themeId);
    }

    @Override
    public Optional<ReservationTime> findById(final long timeId) {
        String sql = RESERVATION_TIME_BASE_SELECT + " WHERE rt.id = ?";

        return jdbcTemplate.query(sql, reservationTimeRowMapper, timeId)
                .stream()
                .findFirst();
    }

    @Override
    public List<ReservationTime> findAvailableTimes(final LocalDate date, final Long themeId) {
        String sql = RESERVATION_TIME_BASE_SELECT + """
                LEFT JOIN reservation r
                       ON r.time_id = rt.id
                      AND r.date = ?
                WHERE rt.theme_id = ?
                  AND r.id IS NULL
                ORDER BY rt.start_at
                """;

        return jdbcTemplate.query(sql, reservationTimeRowMapper, date, themeId);
    }

    @Override
    public void deleteById(final long timeId) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, timeId);
    }

    @Override
    public boolean existsByStartAtAndThemeId(final LocalTime startAt, final long themeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation_time WHERE theme_id = ? AND start_at = ?)";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                themeId,
                Time.valueOf(startAt)
        ));
    }

}
