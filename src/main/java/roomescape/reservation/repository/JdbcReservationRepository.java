package roomescape.reservation.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert reservationInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                """
                SELECT r.id,
                       r.name,
                       r.date,
                       r.time_id,
                       r.theme_id,
                       rt.start_time,
                       rt.end_time,
                       t.name AS theme_name,
                       t.description AS theme_description,
                       t.image_url AS theme_image_url
                FROM reservation r
                LEFT JOIN reservation_time rt ON r.time_id = rt.id
                LEFT JOIN theme t ON r.theme_id = t.id
                """,
                new ReservationRowMapper()
        );
    }

    @Override
    public Reservation save(Reservation reservation) {
        Number id = reservationInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId()));
        return reservation.withId(id.longValue());
    }

    @Override
    public boolean isDuplicated(Long themeId, ReservationTime time, LocalDate date) {
        Integer exists = jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM reservation WHERE theme_id = ? AND time_id = ? AND date = ?)",
                Integer.class,
                themeId,
                time.getId(),
                Date.valueOf(date)
        );
        return exists != null && exists == 1;
    }

    @Override
    public List<Long> findTimeIdsByThemeIdAndDate(Long themeId, LocalDate date) {
        return jdbcTemplate.query(
                "SELECT time_id FROM reservation WHERE theme_id = ? AND date = ?",
                (rs, rowNum) -> rs.getLong("time_id"),
                themeId,
                Date.valueOf(date)
        );
    }

    @Override
    public boolean deleteById(Long id) {
        int affectedRows = jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
        return affectedRows > 0;
    }

    private static class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            ReservationTime time = null;
            Long timeId = rs.getLong("time_id");
            if (!rs.wasNull()) {
                time = new ReservationTime(
                        timeId,
                        rs.getObject("start_time", LocalTime.class),
                        rs.getObject("end_time", LocalTime.class)
                );
            }

            Theme theme = null;
            String themeName = rs.getString("theme_name");
            if (themeName != null) {
                theme = new Theme(
                        themeName,
                        rs.getString("theme_description"),
                        rs.getString("theme_image_url")
                ).withId(rs.getLong("theme_id"));
            }

            Reservation reservation = new Reservation(
                    rs.getString("name"),
                    rs.getDate("date").toLocalDate(),
                    time,
                    theme
            );
            return reservation.withId(rs.getLong("id"));
        }
    }
}

