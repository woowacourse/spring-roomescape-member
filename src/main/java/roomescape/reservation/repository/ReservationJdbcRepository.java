package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.ReservationRepository;
import roomescape.time.domain.ReservationTime;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String sql = "select r.id as reservation_id, r.name, r.date, t.id as time_id, t.start_at as time_value " +
                "from reservation as r " +
                "inner join reservation_time as t " +
                "on r.time_id = t.id";

        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
                new Reservation(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        LocalDate.parse(resultSet.getString("date")),
                        new ReservationTime(
                                resultSet.getLong("time_id"),
                                LocalTime.parse(resultSet.getString("time_value"))
                        )
                ));
    }

    public Reservation save(Reservation reservation) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reservation.getReserverName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTimeId());
        Long id = jdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Reservation(id, reservation.getReserverName(), reservation.getDate(),
                new ReservationTime(reservation.getTimeId(), reservation.getStartAt()));
    }

    public Optional<Reservation> findById(Long id) {
        String sql = "select r.id as reservation_id, r.name, r.date, t.id as time_id, t.start_at as time_value " +
                "from reservation as r " +
                "inner join reservation_time as t " +
                "on r.time_id = t.id " +
                "where r.id = ?";

        Reservation reservation;
        try {
            reservation = jdbcTemplate.queryForObject(sql, (resultSet, rowNum) ->
                    new Reservation(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            LocalDate.parse(resultSet.getString("date")),
                            new ReservationTime(
                                    resultSet.getLong("time_id"),
                                    LocalTime.parse(resultSet.getString("time_value"))
                            )
                    ), id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return Optional.of(Objects.requireNonNull(reservation));
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
