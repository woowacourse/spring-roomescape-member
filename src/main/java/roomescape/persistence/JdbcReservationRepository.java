package roomescape.persistence;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) ->
            new Reservation(
                    rs.getLong("reservation_id"),
                    rs.getString("name"),
                    rs.getDate("date").toLocalDate(),
                    new ReservationTime(rs.getLong("time_id"), rs.getTime("time_value").toLocalTime()),
                    new Theme(rs.getLong("theme_id"), rs.getString("theme_name"), rs.getString("theme_description"), rs.getString("theme_thumbnail"))
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query("SELECT \n"
                        + "    r.id as reservation_id, \n"
                        + "    r.name, \n"
                        + "    r.date, \n"
                        + "    t.id as time_id, \n"
                        + "    t.start_at as time_value, \n"
                        + "    r.theme_id, \n"
                        + "    tm.name as theme_name, \n"
                        + "    tm.description as theme_description, \n"
                        + "    tm.thumbnail as theme_thumbnail \n"
                        + "FROM reservation as r \n"
                        + "inner join reservation_time as t \n"
                        + "on r.time_id = t.id \n"
                        + "inner join theme as tm \n"
                        + "on r.theme_id = tm.id",
                reservationRowMapper);
    }

    @Override
    public Long create(Reservation reservation) {
        String sql = "INSERT INTO reservation(name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate().toString());
            ps.setLong(3, reservation.getTime().id());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void deleteById(Long reservationId) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", reservationId);
    }

    @Override
    public Optional<Reservation> findById(Long reservationId) {
        String sql = """
                SELECT 
                """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(
                    "SELECT \n"
                            + "    r.id as reservation_id, \n"
                            + "    r.name, \n"
                            + "    r.date, \n"
                            + "    t.id as time_id, \n"
                            + "    t.start_at as time_value, \n"
                            + "    r.theme_id, \n"
                            + "    tm.name as theme_name, \n"
                            + "    tm.description as theme_description,\n"
                            + "    tm.thumbnail as theme_thumbnail \n"
                            + "FROM reservation as r \n"
                            + "inner join reservation_time as t \n"
                            + "on r.time_id = t.id \n"
                            + "inner join theme as tm \n"
                            + "on r.theme_id = tm.id \n"
                            + "WHERE r.id = ?",
                    reservationRowMapper, reservationId);
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existByTimeId(final Long reservationTimeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, reservationTimeId) > 0;
    }

    @Override
    public boolean existByDateAndTimeId(final LocalDate reservationDate, final Long timeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE date = ? AND time_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, reservationDate, timeId) > 0;
    }

    @Override
    public boolean existByThemeId(final Long themeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE theme_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, themeId) > 0;
    }

    @Override //TODO: sql 수정 고려
    public List<Reservation> findByThemeIdAndReservationDate(final Long themeId, final LocalDate reservationDate) {
        String sql = "SELECT \n"
                + "    r.id as reservation_id, \n"
                + "    r.name, \n"
                + "    r.date, \n"
                + "    t.id as time_id, \n"
                + "    t.start_at as time_value, \n"
                + "    r.theme_id, \n"
                + "    tm.name as theme_name, \n"
                + "    tm.description as theme_description,\n"
                + "    tm.thumbnail as theme_thumbnail \n"
                + "FROM reservation as r \n"
                + "inner join reservation_time as t \n"
                + "on r.time_id = t.id \n"
                + "inner join theme as tm \n"
                + "on r.theme_id = tm.id \n"
                + "WHERE r.theme_id = ? \n"
                + "AND r.date = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, themeId, reservationDate);
    }
}
