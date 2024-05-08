package roomescape.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.reservation.NotFoundReservationException;

@Repository
public class ReservationDao implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            LocalDate.parse(resultSet.getString("date")),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    LocalTime.parse(resultSet.getString("time_start_at"))
            ),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            )
    );

    public ReservationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT reservation.id, reservation.name, reservation.date, 
                `time`.id AS time_id, `time`.start_at AS time_start_at, 
                theme.id AS theme_id, theme.name AS theme_name, 
                theme.description AS theme_description, theme.thumbnail AS theme_thumbnail 
                FROM reservation 
                INNER JOIN reservation_time AS `time` ON reservation.time_id = `time`.id 
                INNER JOIN theme ON reservation.theme_id = theme.id
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT reservation.id, reservation.name, reservation.date, 
                `time`.id AS time_id, `time`.start_at AS time_start_at, 
                theme.id AS theme_id, theme.name AS theme_name, 
                theme.description AS theme_description, theme.thumbnail AS theme_thumbnail 
                FROM reservation 
                INNER JOIN reservation_time AS `time` ON reservation.time_id = `time`.id 
                INNER JOIN theme ON reservation.theme_id = theme.id
                WHERE reservation.id = ?
                """;
        List<Reservation> reservation = jdbcTemplate.query(sql, reservationRowMapper, id);
        return DataAccessUtils.optionalResult(reservation);
    }

    @Override
    public List<Long> findTimeIdByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT time_id
                FROM reservation
                WHERE date = ? AND theme_id = ?
                """;
        return jdbcTemplate.queryForList(sql, Long.class, date, themeId);
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?)";
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId));
    }

    @Override
    public Reservation save(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", reservation.getName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getId());
        Long id = (Long) jdbcInsert.executeAndReturnKey(parameters);
        return new Reservation(
                id, reservation.getName(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    @Override
    public void delete(Reservation reservation) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        int update = jdbcTemplate.update(sql, reservation.getId());
        checkRemoved(update);
    }

    private void checkRemoved(int count) {
        if (count < 1) {
            throw new NotFoundReservationException();
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM reservation";
        jdbcTemplate.update(sql);
    }
}
