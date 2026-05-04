package roomescape.reservation.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        ReservationTime reservationTime = ReservationTime.of(
                resultSet.getLong("time_id"),
                resultSet.getTime("start_at").toLocalTime()
        );

        return Reservation.of(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                reservationTime
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT r.id,
                       r.name,
                       r.date,
                       rt.id AS time_id,
                       rt.start_at
                FROM reservation AS r
                JOIN reservation_time AS rt ON r.time_id = rt.id
                """;

        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public Optional<Reservation> findById(final long id) {
        String sql = """
                SELECT r.id,
                       r.name,
                       r.date,
                       rt.id AS time_id,
                       rt.start_at
                FROM reservation AS r
                JOIN reservation_time AS rt ON r.time_id = rt.id
                WHERE r.id = ?
                """;

        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, reservationRowMapper, id));
    }

    @Override
    public void deleteById(final long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Reservation save(final Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setDate(2, Date.valueOf(reservation.getDate()));
            preparedStatement.setLong(3, reservation.getTime().getId());
            return preparedStatement;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("[ERROR] 예약 ID를 생성하지 못했습니다.");
        }

        return reservation.withId(key.longValue());
    }

    @Override
    public boolean existsByDateAndTimeId(final LocalDate date, final long timeId) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ?)";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                Date.valueOf(date),
                timeId
        ));
    }

}
