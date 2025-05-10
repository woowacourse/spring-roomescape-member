package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.common.RowMapperManager;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.exception.NotFoundReservationException;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.user.domain.User;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT r.id AS reservation_id,\n" +
                "       r.name,\n" +
                "       r.date,\n" +
                "       time.id AS reservation_time_id,\n" +
                "       time.start_at AS reservation_time_start_at,\n" +
                "       theme.id AS theme_id,\n" +
                "       theme.name AS theme_name,\n" +
                "       theme.description AS theme_description,\n" +
                "       theme.thumbnail AS theme_thumbnail\n" +
                "FROM reservation AS r INNER JOIN reservation_time AS time\n" +
                "    ON r.reservation_time_id = time.id\n" +
                "    INNER JOIN theme AS theme\n" +
                "    ON r.theme_id = theme.id";
        return jdbcTemplate.query(sql, RowMapperManager.reservationRowMapper);
    }

    @Override
    public Reservation findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundReservationException("해당 예약 id가 존재하지 않습니다."));
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT reservation.id AS reservation_id,\n" +
                "       reservation.name AS reservation_name,\n" +
                "       reservation.date AS reservation_date,\n" +
                "       reservation_time.id AS reservation_time_id,\n" +
                "       reservation_time.start_at AS reservation_time_start_at,\n" +
                "       theme.id AS theme_id,\n" +
                "       theme.name AS theme_name,\n" +
                "       theme.description AS theme_description,\n" +
                "       theme.thumbnail AS theme_thumbnail,\n" +
                "       users.id AS user_id,\n" +
                "       users.name AS user_name,\n" +
                "       users.email AS user_email,\n" +
                "       users.password AS user_password\n" +
                "FROM reservation\n" +
                "    JOIN reservation_time\n" +
                "    ON reservation.time_id = reservation_time.id\n" +
                "    JOIN theme\n" +
                "    ON reservation.theme_id = theme.id\n" +
                "    JOIN users\n" +
                "    ON reservation.user_id = users.id\n" +
                "WHERE reservation.id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, RowMapperManager.reservationRowMapper, id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findByThemeAndDate(Theme theme, LocalDate date, User user) {
        String sql =
                "SELECT reservation.id AS reservation_id,\n" +
                        "       reservation.name AS reservation_name,\n" +
                        "       reservation.date AS reservation_date,\n" +
                        "       reservation_time.id AS reservation_time_id,\n" +
                        "       reservation_time.start_at AS reservation_time_start_at,\n" +
                        "       theme.id AS theme_id,\n" +
                        "       theme.name AS theme_name,\n" +
                        "       theme.description AS theme_description,\n" +
                        "       theme.thumbnail AS theme_thumbnail,\n" +
                        "       users.id AS user_id,\n" +
                        "       users.name AS user_name,\n" +
                        "       users.email AS user_email,\n" +
                        "       users.password AS user_password\n" +
                        "FROM reservation\n" +
                        "    JOIN reservation_time\n" +
                        "    ON reservation.time_id = reservation_time.id\n" +
                        "    JOIN theme\n" +
                        "    ON reservation.theme_id = theme.id\n" +
                        "    JOIN users\n" +
                        "    ON reservation.user_id = users.id\n" +
                        "WHERE theme.id = ? AND reservation.date = ? AND users.id = ?";

        return jdbcTemplate.query(sql, RowMapperManager.reservationRowMapper, theme.getId(), date, user.getId());
    }

    @Override
    public Reservation add(Reservation reservation) {
        Long id = insertWithKeyHolder(reservation);
        return findByIdOrThrow(id);
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsByReservationTime(ReservationTime reservationTime) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation JOIN reservation_time ON reservation.id = reservation_time.id WHERE reservation_time.id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, reservationTime.getId());
    }

    @Override
    public boolean existsByDateAndTime(LocalDate date, ReservationTime reservationTime) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = ? AND reservation.time_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, date, reservationTime.getId());
    }

    private Long insertWithKeyHolder(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id, user_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        Long reservationTimeId = reservation.getReservationTime().getId();
        Long themeId = reservation.getTheme().getId();
        Long userId = reservation.getUser().getId();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate().toString());
            ps.setLong(3, reservationTimeId);
            ps.setLong(4, themeId);
            ps.setLong(5, userId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
