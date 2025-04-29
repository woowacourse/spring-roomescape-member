package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;

@Repository
public class H2ReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public H2ReservationRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation findById(final Long id) {
        try {
            final String sql = "SELECT r.id as reservation_id, r.name, r.date,"
                    + " t.id as time_id, t.start_at as time_value "
                    + " FROM reservation as r "
                    + " INNER JOIN reservation_time as t"
                    + " ON r.time_id = t.id "
                    + " WHERE r.id = ?";
            return jdbcTemplate.queryForObject(sql,
                    (resultSet, rowNum) -> Reservation.afterSave(
                            resultSet.getLong("reservation_id"),
                            resultSet.getString("name"),
                            resultSet.getDate("date").toLocalDate(),
                            ReservationTime.afterSave(
                                    resultSet.getLong("time_id"),
                                    resultSet.getTime("time_value").toLocalTime()
                            )
                    ), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Reservation> findAll() {
        final String sql =
                "SELECT r.id as reservation_id ,r.name, r.date,"
                        + " t.id as time_id, t.start_at as time_value"
                        + " FROM reservation as r "
                        + "INNER JOIN reservation_time as t "
                        + "ON r.time_id = t.id";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    Reservation reservation = Reservation.afterSave(
                            resultSet.getLong("reservation_id"),
                            resultSet.getString("name"),
                            resultSet.getDate("date").toLocalDate(),
                            ReservationTime.afterSave(
                                    resultSet.getLong("time_id"),
                                    resultSet.getTime("time_value").toLocalTime()
                            )
                    );
                    return reservation;
                });
    }

    @Override
    public Reservation save(final Reservation reservation) {
        final String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getTime().getId());
            return ps;
        }, keyHolder);

        long generatedId = keyHolder.getKey().longValue();

        return Reservation.afterSave(
                generatedId,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime()
        );
    }

    @Override
    public int deleteById(final Long id) {
        final String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean isDuplicateDateAndTime(LocalDate date, Long timeId) {
        final String sql = "SELECT COUNT(*) FROM reservation as r"
                + " INNER JOIN reservation_time as t"
                + " ON t.id = r.time_id"
                + " WHERE r.date = ? and t.id =?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, date, timeId);
        return count != null && count > 0;
    }

    @Override
    public boolean existByTimeId(Long id) {
        final String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
