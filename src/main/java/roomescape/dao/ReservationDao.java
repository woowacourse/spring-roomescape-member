package roomescape.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.rowmapper.ReservationRowMapper;
import roomescape.dao.rowmapper.ThemeRowMapper;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.Theme;

@Repository
public class ReservationDao implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final ReservationRowMapper reservationRowMapper;
    private final ThemeRowMapper themeRowMapper;

    public ReservationDao(JdbcTemplate jdbcTemplate, DataSource dataSource,
                          ReservationRowMapper reservationRowMapper, ThemeRowMapper themeRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        this.reservationRowMapper = reservationRowMapper;
        this.themeRowMapper = themeRowMapper;
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
    public List<Theme> findThemeWithMostPopularReservation(String startDate, String endDate) {
        String sql = """
                SELECT theme.id, theme.name, theme.description, theme.thumbnail
                FROM reservation
                LEFT JOIN theme ON theme.id=reservation.theme_id
                WHERE reservation.date > ? AND reservation.date < ?
                GROUP BY theme.id
                ORDER BY COUNT(*) DESC
                LIMIT 10;
                """;
        return jdbcTemplate.query(sql, themeRowMapper, startDate, endDate);
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?)";
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = ?)";
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, Boolean.class, timeId));
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE theme_id = ?)";
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, Boolean.class, themeId));
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
        jdbcTemplate.update(sql, reservation.getId());
    }
}
