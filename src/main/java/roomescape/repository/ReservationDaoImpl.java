package roomescape.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDaoImpl implements ReservationDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ReservationDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT r.id AS id, r.name AS name, r.date AS date, t.id AS time_id, t.start_at AS time_value FROM reservation r JOIN reservation_time t ON r.time_id = t.id";
        return jdbcTemplate.query(sql, getReservationRowMapper());
    }

    @Override
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES(:name, :date, :time_id)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("name", reservation.getName())
            .addValue("date", reservation.getDate())
            .addValue("time_id", reservation.getTime().getId());
        jdbcTemplate.update(sql, mapSqlParameterSource, keyHolder);

        Number key = keyHolder.getKey();
        return new Reservation(key.longValue(), reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT r.id AS id, r.name AS name, r.date AS date, t.id AS time_id, t.start_at AS time_value FROM reservation r JOIN reservation_time t ON r.time_id = t.id where r.id = :id ";
        List<Reservation> findReservation = jdbcTemplate.query(
            sql, new MapSqlParameterSource("id", id), getReservationRowMapper());

        return findReservation.stream().findFirst();
    }

    @Override
    public Optional<Reservation> findByTimeId(Long timeId) {
        String sql = "SELECT r.id AS id, r.name AS name, r.date AS date, t.id AS time_id, t.start_at AS time_value FROM reservation r JOIN reservation_time t ON r.time_id = t.id where t.id = :id";
        List<Reservation> findReservation = jdbcTemplate.query(
            sql, new MapSqlParameterSource("id", timeId), getReservationRowMapper());

        return findReservation.stream().findFirst();
    }

    @Override
    public boolean isExist(LocalDate date, Long timeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = :date AND time_id = :time_id)";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("date", date)
            .addValue("time_id", timeId);
        return Boolean.TRUE == jdbcTemplate.queryForObject(sql, mapSqlParameterSource, Boolean.class);
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getObject("date", LocalDate.class),
            new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getObject("time_value", LocalTime.class)
            )
        );
    }
}
