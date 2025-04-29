package roomescape.reservation.infrastructure.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.global.exception.DeleteReservationException;
import roomescape.reservation.application.repository.ReservationRepository;
import roomescape.reservation.application.dto.CreateReservationRequest;
import roomescape.reservation.domain.aggregate.Reservation;
import roomescape.reservation.domain.aggregate.ReservationDate;
import roomescape.reservation.domain.aggregate.ReservationName;
import roomescape.reservation.domain.aggregate.ReservationTime;

@Repository
public class ReservationDao implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation insert(final CreateReservationRequest request) {
        String sql = "insert into reservation (name, date, time_id) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[] {"id"});
            ps.setString(1, request.getName().getName());
            ps.setString(2, request.getDate().getReservationDate().toString());
            ps.setLong(3, request.getTime().getId());
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();

        return new Reservation(id, request.getName(), request.getDate(), request.getTime());
    }

    @Override
    public List<Reservation> findAllReservations() {
        String sql = """
                    SELECT
                    r.id as reservation_id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value
                FROM reservation as r
                inner join reservation_time as t
                on r.time_id = t.id
                """;

        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    return new Reservation(
                            resultSet.getLong("id"),
                            new ReservationName(
                                    resultSet.getString("name")
                            ),
                            new ReservationDate(
                                resultSet.getDate("date").toLocalDate()
                            ),
                            new ReservationTime(
                                    resultSet.getLong("time_id"),
                                    resultSet.getTime("start_at").toLocalTime()
                            ));
                });
    }

    @Override
    public void delete(final Long id) {
        String sql = "delete from reservation where id = ?";
        int rows = jdbcTemplate.update(sql, id);
        if (rows != 1) {
            throw new DeleteReservationException("[ERROR] 삭제하지 못했습니다.");
        }
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE time_id = ?
                )
                """;
        int result = jdbcTemplate.queryForObject(sql,Integer.class, timeId);
        return result == 1;
    }
}
