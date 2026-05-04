package roomescape.domain.reservation;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservationtime.ReservationTime;

@Repository
@RequiredArgsConstructor
public class JdbcReservationRepository implements ReservationRepository {

    private static final String INSERT_SQL = "insert into reservation(name, date, time_id) values (?, ?, ?)";
    private static final String FIND_ALL_SQL =
        """
            select r.id, r.name, r.date, rt.id as time_id, rt.start_at
            from reservation r
            join reservation_time rt on r.time_id = rt.id
            order by r.id
            """;
    private static final String COUNT_BY_TIME_ID_SQL =
        """
            select count(*)
            from reservation
            where time_id = ?
            """;
    private static final String DELETE_BY_ID_SQL = "delete from reservation where id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate().toString());
            ps.setLong(3, reservation.getTime().getId());
            return ps;
        }, keyHolder);
        long id = extractId(keyHolder);
        return Reservation.of(
            id,
            reservation.getName(),
            reservation.getDate(),
            reservation.getTime()
        );
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, reservationRowMapper());
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update(DELETE_BY_ID_SQL, id);
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (rs, rowNum) -> Reservation.of(
            rs.getLong("id"),
            rs.getString("name"),
            LocalDate.parse(rs.getString("date")),
            ReservationTime.of(
                rs.getLong("time_id"),
                LocalTime.parse(rs.getString("start_at"))
            )
        );
    }

    private long extractId(KeyHolder keyHolder) {
        if (keyHolder.getKey() == null) {
            throw new IllegalStateException("생성 키를 조회할 수 없습니다.");
        }
        return keyHolder.getKey().longValue();
    }

    @Override
    public int countByTimeId(Long timeId) {
        Integer count = jdbcTemplate.queryForObject(COUNT_BY_TIME_ID_SQL, Integer.class, timeId);
        if (count == null) {
            return 0;
        }
        return count;
    }
}
