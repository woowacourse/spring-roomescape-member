package roomescape.dao;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.dto.time.BookableTimeResponse;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public ReservationTime insert(ReservationTime time) {
        LocalTime startAt = time.getStartAt();
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start_at", startAt.toString());

        Long id = insertActor.executeAndReturnKey(parameters).longValue();

        return new ReservationTime(id, startAt);
    }

    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, getReservationTimeRowMapper());
    }

    public Optional<ReservationTime> findById(Long id) {
        String sql = """
                SELECT 
                    id, 
                    start_at 
                FROM reservation_time 
                WHERE id = ?
                LIMIT 1
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, getReservationTimeRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Boolean hasSameTime(LocalTime time) {
        String sql = "SELECT EXISTS(SELECT start_at FROM reservation_time WHERE start_at = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, time.toString());
    }

    public List<BookableTimeResponse> getAllBookableTime(String date, Long themeId) {
        String sql = """
                SELECT 
                    t.id,
                    t.start_at,
                    CASE 
                        WHEN r.id IS NOT NULL THEN TRUE
                        ELSE FALSE
                    END AS booked
                FROM reservation_time AS t
                LEFT JOIN reservation AS r
                    ON t.id = r.time_id
                    AND r.date = ?
                    AND r.theme_id = ?
                """;

        return jdbcTemplate.query(sql, getBookableTimeRowMapper(), date, themeId);
    }

    private RowMapper<ReservationTime> getReservationTimeRowMapper() {
        return (resultSet, numRow) -> new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime()
        );
    }

    private RowMapper<BookableTimeResponse> getBookableTimeRowMapper() {
        return (resultSet, rowNum) -> new BookableTimeResponse(
                resultSet.getLong("id"),
                resultSet.getString("start_at"),
                resultSet.getBoolean("booked")
        );
    }
}
