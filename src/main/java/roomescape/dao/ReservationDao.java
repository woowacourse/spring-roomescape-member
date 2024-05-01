package roomescape.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationDao implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation_id"),
            resultSet.getString("name"),
            LocalDate.parse(resultSet.getString("date")),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    LocalTime.parse(resultSet.getString("time_value"))
            )
    );

    public ReservationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT r.id AS reservation_id, r.name, r.date, t.id AS time_id, t.start_at AS time_value "
                + "FROM reservation AS r INNER JOIN reservation_time AS t ON r.time_id = t.id";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT r.id AS reservation_id, r.name, r.date, t.id AS time_id, t.start_at AS time_value "
                + "FROM reservation AS r INNER JOIN reservation_time AS t ON r.time_id = t.id WHERE r.id = ?";
        Reservation reservation = jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
        return Optional.ofNullable(reservation);
    }

    @Override
    public Boolean existsByDateAndTimeId(LocalDate date, Long timeId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = ? AND time_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId);
    }

    @Override
    public Reservation save(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", reservation.getName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        Long id = (Long) jdbcInsert.executeAndReturnKey(parameters);
        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    @Override
    public void delete(Reservation reservation) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, reservation.getId());
    }
}
