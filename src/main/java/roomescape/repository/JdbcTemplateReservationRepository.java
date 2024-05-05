package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.rowmapper.ReservationRowMapper;

@Repository
public class JdbcTemplateReservationRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ReservationRowMapper reservationRowMapper;

    public JdbcTemplateReservationRepository(JdbcTemplate jdbcTemplate, ReservationRowMapper reservationRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationRowMapper = reservationRowMapper;
    }

    @Override
    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        save(reservation, keyHolder);
        long id = keyHolder.getKey().longValue();
        return new Reservation(id, reservation);
    }

    private void save(Reservation reservation, KeyHolder keyHolder) {
        jdbcTemplate.update(con -> {
            String sql = "INSERT INTO reservation(name, date, time_id, theme_id) VALUES ( ?,?,?,? )";
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setDate(2, Date.valueOf(reservation.getDate()));
            preparedStatement.setLong(3, reservation.getReservationTime().getId());
            preparedStatement.setLong(4, reservation.getTheme().getId());
            return preparedStatement;
        }, keyHolder);
    }

    @Override
    public List<Reservation> findAll() {
        String query = """
                SELECT
                   r.id AS reservation_id,
                   r.name AS reservation_name,
                   r.date AS reservation_date,
                   t.id AS time_id,
                   t.start_at AS time_value,
                   t2.id AS theme_id,
                   t2.name AS theme_name,
                   t2.description AS description,
                   t2.thumbnail AS thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time t
                    ON r.time_id = t.id
                INNER JOIN theme t2
                    ON t2.id = r.theme_id
                """;
        return jdbcTemplate.query(query, reservationRowMapper);
    }

    @Override
    public boolean existsByThemeAndDateAndTime(Theme theme, LocalDate date, ReservationTime reservationTime) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE theme_id = ? AND date = ? AND time_id = ?)";
        long themeId = theme.getId();
        long timeId = reservationTime.getId();
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, themeId, date, timeId));
    }

    @Override
    public boolean existsByTime(ReservationTime reservationTime) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = ?)";
        long timeId = reservationTime.getId();
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, timeId));
    }

    @Override
    public boolean existsByTheme(Theme theme) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE theme_id = ?)";
        long themeId = theme.getId();
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, themeId));
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }
}
