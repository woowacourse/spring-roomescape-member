package roomescape.repository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.command.ReservationEditCommand;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.ConflictException;
import roomescape.exception.code.ConflictCode;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTemplateReservationRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        try {
            Reservation reservation = jdbcTemplate.queryForObject(
                    "SELECT r.id AS reservation_id, r.name AS reservation_name, r.date, " +
                            "t.id AS time_id, t.start_at, " +
                            "th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, " +
                            "th.thumbnail_url AS theme_thumbnail_url " +
                            "FROM reservation r " +
                            "JOIN reservation_time t ON r.time_id = t.id " +
                            "JOIN theme th ON r.theme_id = th.id " +
                            "WHERE r.id = ?",
                    reservationRowMapper(),
                    id
            );
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
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
        try {
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
        } catch (DuplicateKeyException e) {
            throw new ConflictException(ConflictCode.RESERVATION_DUPLICATED);
        }

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
    public void updateCancelled(Long id) {
        int archived = jdbcTemplate.update(
                "INSERT INTO canceled_reservation (id, name, date, time_id, theme_id) " +
                        "SELECT id, name, date, time_id, theme_id FROM reservation WHERE id = ?",
                id);
        if (archived == 0) {
            throw new EmptyResultDataAccessException(1);
        }
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

    @Override
    public int countReservationsOf(LocalDate date, long timeId, long themeId) {
        return Objects.requireNonNull(jdbcTemplate.queryForObject(
                "SELECT COUNT(*) cnt FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?",
                Integer.class,
                date, timeId, themeId));
    }

    @Override
    public void updateReservation(Long id, ReservationEditCommand command) {
        try {
            jdbcTemplate.update(
                    "UPDATE reservation SET date = ?, time_id = ? WHERE id = ?",
                    command.date(), command.timeId(), id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException();
        }
    }
}
