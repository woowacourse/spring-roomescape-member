package roomescape.reservation.domain.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

@Repository
public class JdbcReservationDao implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> rowMapper =
            (rs, rowNum) -> {
                Long reservationId = rs.getLong("reservation_id");
                String name = rs.getString("name");
                LocalDate date = LocalDate.parse(rs.getString("date"));
                Long timeId = rs.getLong("time_id");
                LocalTime startAt = LocalTime.parse(rs.getString("start_at"));
                ReservationTime time = new ReservationTime(timeId, startAt);

                Long themeId = rs.getLong("theme_id");
                String themeName = rs.getString("theme_name");
                String themeDescription = rs.getString("theme_des");
                String themeThumbnail = rs.getString("theme_thumb");
                Theme theme = new Theme(themeId, themeName, themeDescription, themeThumbnail);

                return new Reservation(reservationId, name, date, time, theme);
            };

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveAndReturnId(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, reservation.getName());
                    preparedStatement.setString(2, reservation.getDate().toString());
                    preparedStatement.setLong(3, reservation.getTime().getId());
                    preparedStatement.setLong(4, reservation.getTheme().getId());
                    return preparedStatement;
                },
                keyHolder
        );

        return keyHolder.getKey().longValue();
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_des,
                    th.thumbnail AS theme_thumb
                FROM reservation AS r
                INNER JOIN reservation_time AS t 
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Boolean existReservationByTimeId(Long timeId) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM reservation
                    WHERE time_id = ?
                )
                """;

        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    @Override
    public Boolean existReservationByThemeId(Long themeId) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM reservation
                    WHERE theme_id = ?
                )
                """;

        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
    }

    @Override
    public Boolean existReservationByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation AS r
                    INNER JOIN reservation_time AS t
                    ON r.time_id = t.id
                    INNER JOIN theme AS th  
                    ON r.theme_id = th.id
                    WHERE r.date = ? AND t.id = ? AND th.id = ?
                );                  
                """;

        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_des,
                    th.thumbnail AS theme_thumb
                FROM reservation AS r
                INNER JOIN reservation_time AS t 
                ON r.time_id = t.id
                INNER JOIN theme AS th
                ON r.theme_id = th.id
                WHERE r.date = ? AND r.theme_id = ?
                """;

        return jdbcTemplate.query(sql, rowMapper, date, themeId);
    }
}
