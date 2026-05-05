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
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

@Repository
@RequiredArgsConstructor
public class JdbcReservationRepository implements ReservationRepository {

    private static final String INSERT_SQL = "insert into reservation(name, date_id, time_id, theme_id) values (?, ?, ?, ?)";
    private static final String FIND_ALL_SQL =
        """
            select r.id, r.name,
                   rd.id as date_id, rd.date,
                   rt.id as time_id, rt.start_at,
                   th.id as theme_id, th.name as theme_name, th.content as theme_content, th.url as theme_url
            from reservation r
            join reservation_date rd on r.date_id = rd.id
            join reservation_time rt on r.time_id = rt.id
            join theme th on r.theme_id = th.id
            order by r.id
            """;
    private static final String COUNT_BY_TIME_ID_SQL =
        """
            select count(*)
            from reservation
            where time_id = ?
            """;
    private static final String COUNT_BY_RESERVATION_DATE_ID_SQL =
        """
            select count(*)
            from reservation
            where date_id = ?
            """;
    private static final String DELETE_BY_ID_SQL = "delete from reservation where id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, reservation.getName());
            ps.setLong(2, reservation.getDate().getId());
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);
        long id = extractId(keyHolder);
        return Reservation.of(
            id,
            reservation.getName(),
            reservation.getDate(),
            reservation.getTime(),
            reservation.getTheme()
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

    @Override
    public int countByTimeId(Long timeId) {
        Integer count = jdbcTemplate.queryForObject(COUNT_BY_TIME_ID_SQL, Integer.class, timeId);
        if (count == null) {
            return 0;
        }
        return count;
    }

    @Override
    public int countByReservationDateId(Long dateId) {
        Integer count = jdbcTemplate.queryForObject(COUNT_BY_RESERVATION_DATE_ID_SQL, Integer.class, dateId);
        if (count == null) {
            return 0;
        }
        return count;
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (rs, rowNum) -> Reservation.of(
            rs.getLong("id"),
            rs.getString("name"),
            ReservationDate.of(
                rs.getLong("date_id"),
                LocalDate.parse(rs.getString("date"))),
            ReservationTime.of(
                rs.getLong("time_id"),
                LocalTime.parse(rs.getString("start_at"))
            ),
            Theme.of(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("theme_content"),
                rs.getString("theme_url")
            )
        );
    }

    private long extractId(KeyHolder keyHolder) {
        if (keyHolder.getKey() == null) {
            throw new IllegalStateException("생성 키를 조회할 수 없습니다.");
        }
        return keyHolder.getKey().longValue();
    }
}
