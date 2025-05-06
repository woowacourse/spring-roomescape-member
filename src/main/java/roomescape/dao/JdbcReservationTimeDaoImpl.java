package roomescape.dao;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.dto.BookedReservationTimeResponseDto;

@Repository
public class JdbcReservationTimeDaoImpl implements ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcReservationTimeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
            .withTableName("reservation_time")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAll() {
        String query = "select * from reservation_time";
        return jdbcTemplate.query(query,
            (resultSet, RowNum) -> {
                ReservationTime reservationTime = new ReservationTime(
                    resultSet.getLong("id"),
                    LocalTime.parse(resultSet.getString("start_at")));
                return reservationTime;
            });
    }

    @Override
    public long save(ReservationTime reservationTime) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("start_at", reservationTime.getStartAt());
        Number newId = insertActor.executeAndReturnKey(parameters);
        return newId.longValue();
    }

    @Override
    public void delete(Long id) {
        String query = "delete from reservation_time where id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String query = "select * from reservation_time where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query,
                (resultSet, rowNum) -> {
                    return new ReservationTime(
                        resultSet.getLong("id"),
                        LocalTime.parse(resultSet.getString("start_at")));
                }, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<BookedReservationTimeResponseDto> findBooked(String date,
        Long themeId) {
        String query = """
            SELECT
                rt.START_AT,
                rt.id AS TIME_ID,
                CASE
                    WHEN r.id IS NULL THEN false
                    ELSE true
                END AS already_booked
            FROM 
                reservation_time rt
            LEFT JOIN 
                reservation r 
                ON r.time_id = rt.id
                AND r.date = ?
                AND r.theme_id = ?;
            """;
        return jdbcTemplate.query(query,
            (resultSet, rowNum) -> {
                return new BookedReservationTimeResponseDto(
                    resultSet.getString("start_at"),
                    resultSet.getLong("time_id"),
                    resultSet.getBoolean("already_booked")
                );
            }, date, themeId);
    }
}
