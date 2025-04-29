package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.globalException.CustomException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.repository.ReservationTimeRepository;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationRepositoryImpl(final JdbcTemplate jdbcTemplate,
                                     ReservationTimeRepository reservationTimeRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT \n" +
            "    r.id as reservation_id, \n" +
            "    r.name, \n" +
            "    r.date, \n" +
            "    t.id as time_id, \n" +
            "    t.start_at as time_value \n" +
            "FROM reservation as r \n" +
            "inner join reservation_time as t \n" +
            "on r.time_id = t.id\n";
        return jdbcTemplate.query(
            sql,
            (resultSet, rowNum) -> {
                ReservationTime time = new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("time_value").toLocalTime()
                );

                Reservation reservation = new Reservation(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getDate("date").toLocalDate(),
                    time
                );
                return reservation;
            });
    }

    @Override
    public Reservation findByIdOrThrow(Long id) {
        return findById(id)
            .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "해당 예약 id가 존재하지 않습니다."));
    }

    @Override
    public Reservation add(Reservation reservation) {
        Long id = insertWithKeyHolder(reservation);
        return findById(id).get();
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    private Long insertWithKeyHolder(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        Long reservationTimeId = reservation.getReservationTime().getId();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                sql,
                new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate().toString());
            ps.setLong(3, reservationTimeId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    private Optional<Reservation> findById(Long id) {
        String sql = "select id, name, date, time_id from reservation where id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
                (resultSet, rowNum) -> new Reservation(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getDate("date").toLocalDate(),
                    reservationTimeRepository.findByIdOrThrow(resultSet.getLong("time_id"))
                ), id));

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
