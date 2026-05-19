package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation create(Reservation reservationWithoutId) {
        String sql = """
                INSERT INTO reservation(name, date, time_id, theme_id)
                VALUES (?, ?, ?, ?)
                """;

        ReservationTime time = reservationWithoutId.getTime();
        Theme theme = reservationWithoutId.getTheme();

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, reservationWithoutId.getName());
            preparedStatement.setDate(2, Date.valueOf(reservationWithoutId.getDate()));
            preparedStatement.setLong(3, time.getId());
            preparedStatement.setLong(4, theme.getId());

            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return Reservation.of(id, reservationWithoutId);
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT r.id, r.name, r.date,
                       t.id AS time_id, t.start_at AS time_value,
                       th.id AS theme_id, th.name AS theme_name,
                       th.description AS theme_description, th.thumbnail_url AS theme_thumbnail_url
                FROM reservation r
                INNER JOIN reservation_time t ON r.time_id = t.id
                INNER JOIN theme th ON r.theme_id = th.id
                """;

        return jdbcTemplate.query(sql, this::mapToReservation);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT r.id, r.name, r.date,
                       t.id AS time_id, t.start_at AS time_value,
                       th.id AS theme_id, th.name AS theme_name,
                       th.description AS theme_description, th.thumbnail_url AS theme_thumbnail_url
                FROM reservation r
                INNER JOIN reservation_time t ON r.time_id = t.id
                INNER JOIN theme th ON r.theme_id = th.id
                WHERE r.id = ?
                """;

        return jdbcTemplate
                .query(sql, this::mapToReservation, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Reservation> findAllByName(String name) {
        String sql = """
                SELECT r.id, r.name, r.date,
                       t.id AS time_id, t.start_at AS time_value,
                       th.id AS theme_id, th.name AS theme_name,
                       th.description AS theme_description, th.thumbnail_url AS theme_thumbnail_url
                FROM reservation r
                INNER JOIN reservation_time t ON r.time_id = t.id
                INNER JOIN theme th ON r.theme_id = th.id
                WHERE r.name = ?
                """;

        return jdbcTemplate.query(sql, this::mapToReservation, name);
    }

    @Override
    public void update(Reservation reservation) {
        String sql = """
                UPDATE reservation
                SET date = ?, time_id = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(sql,
                Date.valueOf(reservation.getDate()),
                reservation.getTime().getId(),
                reservation.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = """
                DELETE FROM reservation
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE date = ?
                      AND time_id = ?
                      AND theme_id = ?
                )
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId));
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeIdExcludingId(LocalDate date, Long timeId, Long themeId,
                                                              Long excludeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE date = ?
                      AND time_id = ?
                      AND theme_id = ?
                      AND id <> ?
                )
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId, excludeId));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE time_id = ?
                )
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, timeId));
    }

    @Override
    public boolean existsByThemeId(Long timeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE theme_id = ?
                )
                """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, timeId));
    }

    private Reservation mapToReservation(ResultSet resultSet, int rowNum) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        LocalDate date = resultSet.getDate("date").toLocalDate();
        Long timeId = resultSet.getLong("time_id");
        LocalTime timeValue = resultSet.getTime("time_value").toLocalTime();
        Long themeId = resultSet.getLong("theme_id");
        String themeName = resultSet.getString("theme_name");
        String themeDescription = resultSet.getString("theme_description");
        String themeThumbnailUrl = resultSet.getString("theme_thumbnail_url");

        ReservationTime reservationTime = new ReservationTime(timeId, timeValue);
        Theme theme = new Theme(themeId, themeName, themeDescription, themeThumbnailUrl);
        return new Reservation(id, name, date, reservationTime, theme);
    }
}