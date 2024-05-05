package roomescape.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservationtime.ReservationStartAt;
import roomescape.domain.reservationtime.ReservationTime;

@Repository
public class WebReservationTimeDao implements ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;

    public WebReservationTimeDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<ReservationTime> readAll() {
        String sql = "SELECT id, start_at FROM reservation_time" ;
        return jdbcTemplate.query(sql, reservationTimeRowMapper());
    }

    @Override
    public Optional<ReservationTime> readById(long id) {
        String sql = "SELECT  id, start_at FROM reservation_time WHERE id = ? " ;
        try {
            ReservationTime reservationTime = jdbcTemplate.queryForObject(sql, reservationTimeRowMapper(), id);
            return Optional.of(reservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public ReservationTime create(ReservationTime reservationTime) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("reservation_time").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("start_at", reservationTime.getStartAt().getValue());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new ReservationTime(id, reservationTime.getStartAt());
    }

    @Override
    public Boolean exist(long id) {
        String sql = """
                SELECT
                CASE
                    WHEN EXISTS (SELECT 1 FROM reservation_time WHERE id = ?)
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public Boolean exist(ReservationTime reservationTime) {
        String sql = """
                SELECT
                CASE
                    WHEN EXISTS (SELECT 1 FROM reservation_time WHERE start_at = ?)
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, reservationTime.getStartAt().toStringTime());
    }

    @Override
    public void delete(long id) {
        String sql = """
                DELETE
                FROM reservation_time
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return (resultSet, rowNum) -> new ReservationTime(
                resultSet.getLong("id"),
                ReservationStartAt.from(resultSet.getString("start_at"))
        );
    }
}
