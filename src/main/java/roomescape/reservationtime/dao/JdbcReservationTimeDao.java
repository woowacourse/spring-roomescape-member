package roomescape.reservationtime.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.dto.response.AvailableTimeResponse;

@Repository
public class JdbcReservationTimeDao implements ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "select * from reservation_time ORDER BY start_at";
        return this.jdbcTemplate.query(sql,
                (resultSet, rowNum) -> {
                    String timeString = resultSet.getString("start_at");

                    return new ReservationTime(
                            resultSet.getLong("id"),
                            LocalTime.parse(timeString)
                    );
                });
    }

    @Override
    public ReservationTime create(ReservationTime reservationTime) {
        String sql = "insert into reservation_time (start_at) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        this.jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            LocalTime startAt = reservationTime.getStartAt();
            ps.setString(1, startAt.toString());
            return ps;
        }, keyHolder);

        return reservationTime.withId(keyHolder.getKey().longValue());
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from reservation_time where id = ?";
        this.jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "select * from reservation_time where id = ?";

        try {
            ReservationTime foundReservationTime = this.jdbcTemplate.queryForObject(sql,
                    (resultSet, rowNum) -> {
                        ReservationTime reservationTime = new ReservationTime(
                                resultSet.getLong("id"),
                                resultSet.getObject("start_at", LocalTime.class)
                        );
                        return reservationTime;
                    }, id
            );
            return Optional.ofNullable(foundReservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<AvailableTimeResponse> findByDateAndThemeIdWithBooked(LocalDate date, Long themeId) {
        String sql = """
                SELECT rt.id, rt.start_at, r.id is not null as already_booked
                FROM reservation_time rt
                LEFT JOIN reservation r
                  ON rt.id = r.time_id
                  AND r.date = ?
                  AND r.theme_id = ?
                """;
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new AvailableTimeResponse(
                        rs.getLong("id"),
                        rs.getTime("start_at").toLocalTime(),
                        rs.getBoolean("already_booked")
                ),
                date, themeId
        );
    }
}
