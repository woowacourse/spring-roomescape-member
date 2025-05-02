package roomescape.repository.reservationtime;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.dto.other.TimeWithBookState;

@Repository
public class H2ReservationTimeRepository implements ReservationTimeRepository {

    private static final RowMapper<ReservationTime> reservationTimeMapper;
    private static final RowMapper<TimeWithBookState> reservationTimeWithBookStateMapper;
    private final JdbcTemplate template;

    static {
        reservationTimeMapper = (resultSet, resultNumber) -> new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime()
        );
        reservationTimeWithBookStateMapper = (resultSet, resultNumber) -> new TimeWithBookState(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime(),
                resultSet.getBoolean("book_state")
        );
    }

    public H2ReservationTimeRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT * FROM reservation_time";
        return template.query(sql, reservationTimeMapper);
    }

    @Override
    public Optional<ReservationTime> findById(long id) {
        String sql = "SELECT * FROM reservation_time WHERE reservation_time.id = ?";
        try {
            ReservationTime reservationTime = template.queryForObject(sql, reservationTimeMapper, id);
            return Optional.of(reservationTime);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public boolean findByStartAt(LocalTime startAt) {
        String sql = "SELECT EXISTS ( "
                + "SELECT * "
                + "FROM reservation_time AS rt "
                + "WHERE rt.start_at = ?)";
        return template.queryForObject(sql, Boolean.class, startAt);
    }

    @Override
    public List<TimeWithBookState> findAllWithBookState(LocalDate date, long themeId) {
        String sql = "SELECT "
                + "rt.id, "
                + "rt.start_at, "
                + "EXISTS ("
                + "SELECT * "
                + "FROM reservation AS r "
                + "WHERE r.time_id = rt.id "
                + "AND r.date = ? "
                + "AND r.theme_id = ? "
                + ") AS book_state "
                + "FROM reservation_time AS rt ";
        return template.query(sql, reservationTimeWithBookStateMapper, date, themeId);
    }

    @Override
    public long add(ReservationTime reservationTime) {
        String sql = "INSERT INTO reservation_time (start_at) values (?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(
                (connection) -> {
                    PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setTime(1, Time.valueOf(reservationTime.getStartAt()));
                    return statement;
                },
                keyHolder
        );
        return keyHolder.getKey().longValue();
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM reservation_time WHERE reservation_time.id = ?";
        template.update(sql, id);
    }
}
