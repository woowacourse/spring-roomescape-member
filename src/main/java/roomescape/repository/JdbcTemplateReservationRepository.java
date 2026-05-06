package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcTemplateReservationRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAllReservations() {
        return jdbcTemplate.query(
                "SELECT r.id AS reservation_id, r.name AS reservation_name, r.date, " +
                        "t.id AS time_id, t.start_at, " +
                        "th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, " +
                        "th.thumbnail_url AS theme_thumbnail_url " +
                        "FROM reservation r " +
                        "JOIN reservation_time t ON r.time_id = t.id " +
                        "JOIN theme th ON r.theme_id = th.id",
                reservationRowMapper()
        );
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (rs, rowNum) -> {
            ReservationTime reservationTime = new ReservationTime(
                    rs.getLong("time_id"),
                    rs.getTime("start_at").toLocalTime());
            Theme theme = new Theme(
                    rs.getLong("theme_id"),
                    rs.getString("theme_name"),
                    rs.getString("theme_description"),
                    rs.getString("theme_thumbnail_url"));
            return new Reservation(
                    rs.getLong("reservation_id"),
                    rs.getString("reservation_name"),
                    rs.getDate("date").toLocalDate(),
                    reservationTime,
                    theme);
        };
    }

    @Override
    public Reservation addReservation(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                conn -> {
                    PreparedStatement preparedStatement = conn.prepareStatement(
                            "INSERT INTO reservation(name, date, time_id, theme_id) " +
                                    "VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, reservation.name());
                    preparedStatement.setDate(2, java.sql.Date.valueOf(reservation.date()));
                    preparedStatement.setLong(3, reservation.timeId());
                    preparedStatement.setLong(4, reservation.themeId());

                    return preparedStatement;
                },
                keyHolder);

        return new Reservation(
                Objects.requireNonNull(keyHolder.getKey()).longValue(),
                reservation.name(),
                reservation.date(),
                reservation.time(),
                reservation.theme());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    @Override
    public List<Reservation> findReservationsByName(String name) {
        return jdbcTemplate.query(
                "SELECT r.id AS reservation_id, r.name AS reservation_name, r.date, " +
                        "t.id AS time_id, t.start_at, " +
                        "th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, " +
                        "th.thumbnail_url AS theme_thumbnail_url " +
                        "FROM reservation r " +
                        "JOIN reservation_time t ON r.time_id = t.id " +
                        "JOIN theme th ON r.theme_id = th.id " +
                        "WHERE r.name = ?",
                reservationRowMapper(),
                name
        );
    }
}
