package roomescape.domain.reservation.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.repository.ReservationRepository;

@Repository
public class ReservationDAO implements ReservationRepository {

    private static final String TABLE_NAME = "reservation";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ReservationDAO(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                select rs.id as reservation_id, rs.name, rs.date, rst.id as reservation_time_id, rst.start_at
                from reservation rs
                INNER JOIN reservation_time rst ON rs.time_id = rst.id
                """;

        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> reservationOf(resultSet)
        );
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                select rs.id as reservation_id, rs.name, rs.date, rst.id  as reservation_time_id, rst.start_at
                from reservation rs
                inner join reservation_time rst on rs.time_id = rst.id where rs.id = :reservation_id
                """;

        Map<String, Long> params = Map.of("reservation_id", id);

        return reservationOf(sql, params);
    }

    private Optional<Reservation> reservationOf(String sql, Map<String, Long> params) {
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql,
                    params,
                    (resultSet, rowNum) -> reservationOf(resultSet));
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("entity not found");
        }
    }

    private Reservation reservationOf(ResultSet resultSet) throws SQLException {
        return new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                LocalDate.parse(resultSet.getString("date")),
                reservationTimeOf(resultSet));
    }

    private ReservationTime reservationTimeOf(ResultSet resultSet) throws SQLException {
        return new ReservationTime(
                resultSet.getLong("reservation_time_id"),
                LocalTime.parse(resultSet.getString("start_at")
                ));
    }

    @Override
    public Reservation save(Reservation reservation) {

        if (reservation.existId()) {
            return update(reservation);
        }

        return create(reservation);
    }

    private Reservation create(Reservation reservation) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getReservationDate())
                .addValue("time_id", reservation.getReservationTimeId());

        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(id, reservation.getName(), reservation.getReservationDate(),
                reservation.getReservationTime());
    }

    private Reservation update(Reservation reservation) {
        String updateReservationSql = "update reservation set name = :name, date = :date, time_id = :time_id where id =:id";
        checkReservationTime(reservation.getReservationTime());

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getReservationDate())
                .addValue("time_id", reservation.getReservationTimeId())
                .addValue("id", reservation.getId());

        int updatedRowCount = jdbcTemplate.update(updateReservationSql, params);

        if (updatedRowCount == 0) {
            throw new EntityNotFoundException("Reservation with id " + reservation.getId() + " not found");
        }

        return reservation;
    }

    private void checkReservationTime(ReservationTime reservationTime) {
        if (reservationTime == null) {
            throw new EntityNotFoundException("reservationTime is null");
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from reservation where id = :id";
        Map<String, Long> params = Map.of("id", id);

        int updatedRowCount = jdbcTemplate.update(sql, params);

        if (updatedRowCount != 1) {
            throw new EntityNotFoundException("Reservation with id " + id + " not found");
        }
    }

    @Override
    public boolean existsByDateAndTimeId(LocalDate date, Long timeId) {
        String sql = "select count(*) from reservation where date = :date and time_id = :time_id";
        Map<String, Object> params = Map.of("date", date, "time_id", timeId);

        int count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != 0;
    }
}
