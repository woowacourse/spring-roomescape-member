package roomescape.reservation;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Repository
public class ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> rowMapper = (rs, rowNum) -> new Reservation(
            rs.getLong("reservation_id"),
            rs.getString("name"),
            rs.getObject("date", LocalDate.class),
            new ReservationTime(rs.getLong("time_id"), rs.getObject("time_value", LocalTime.class)),
            new Theme(rs.getLong("theme_id"), rs.getString("theme_name"), rs.getString("theme_description"),
                    rs.getString("theme_thumbnail"))
    );


    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Reservation save(String name, LocalDate date, ReservationTime time, Theme theme) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setObject(2, date);
            ps.setLong(3, time.id());
            ps.setLong(4, theme.id());
            return ps;
        }, keyHolder);

        long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return new Reservation(generatedId, name, date, time, theme);
    }

    public List<Reservation> findAll() {
        String sql = "SELECT r.id AS reservation_id, r.name, r.date, " +
                "rt.id AS time_id, rt.start_at AS time_value, " +
                "th.id AS theme_id, th.name AS theme_name, " +
                "th.description AS theme_description, th.thumbnail AS theme_thumbnail " +
                "FROM reservation AS r " +
                "INNER JOIN reservation_time AS rt ON r.time_id = rt.id " +
                "INNER JOIN themes AS th ON r.theme_id = th.id";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public void delete(long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public int countByTimeId(long timeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, timeId);
    }

    public List<Long> findByDateAndTheme(LocalDate date, long themeId) {
        String sql = "SELECT time_id FROM reservation WHERE date = ? AND theme_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, date, themeId);
    }

    public Optional<Reservation> findById(long id) {
        String sql = "SELECT r.id AS reservation_id, r.name, r.date, " +
                "rt.id AS time_id, rt.start_at AS time_value, " +
                "th.id AS theme_id, th.name AS theme_name, " +
                "th.description AS theme_description, th.thumbnail AS theme_thumbnail " +
                "FROM reservation AS r " +
                "INNER JOIN reservation_time AS rt ON r.time_id = rt.id " +
                "INNER JOIN themes AS th ON r.theme_id = th.id " +
                "WHERE r.id = ?";

        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<Long> findReservedTimeIds(LocalDate date, long themeId) {
        String sql = "SELECT DISTINCT time_id FROM reservation WHERE date = ? AND theme_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, date, themeId);
    }
}
