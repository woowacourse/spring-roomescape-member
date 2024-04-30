package roomescape.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> rowMapper;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("date"),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getString("start_at"))
        );
    }

    public List<Reservation> readReservations() {
        String sql = """
                SELECT reservation.id, reservation.name, reservation.date, reservation.time_id, reservation_time.start_at
                FROM reservation
                JOIN reservation_time ON reservation.time_id = reservation_time.id;
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public boolean isReservationsByTimeId(long time_id) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE time_id = ?
                )
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, time_id);
    }

    public Optional<Reservation> readReservationByTimeId(long time_id) {
        String sql = """
                SELECT reservation.id, reservation.name, reservation.date, reservation.time_id, reservation_time.start_at
                FROM reservation
                JOIN reservation_time ON reservation.time_id = reservation_time.id
                WHERE reservation.time_id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, time_id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    private Optional<Reservation> readReservationById(long id) {
        String sql = """
                SELECT reservation.id, reservation.name, reservation.date, reservation.time_id, reservation_time.start_at
                FROM reservation
                JOIN reservation_time ON reservation.time_id = reservation_time.id
                WHERE reservation.id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Reservation createReservation(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO reservation (name, date, time_id) values (?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setString(2, reservation.getDate());
            preparedStatement.setLong(3, reservation.getTime().getId());
            return preparedStatement;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        return readReservationById(id).orElseThrow();
    }

    public void deleteReservation(long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
