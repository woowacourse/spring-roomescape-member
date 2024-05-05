package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<ReservationTime> reservationTimeRowMapper = (resultSet, __) -> new ReservationTime(
            resultSet.getLong("id"),
            resultSet.getTime("start_at").toLocalTime()
    );

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("SELECT * FROM RESERVATION_TIME", reservationTimeRowMapper);
    }

    public ReservationTime findById(long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM RESERVATION_TIME WHERE ID = ?", reservationTimeRowMapper, id
        );
    }

    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.startAt());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return findById(id);
    }

    public boolean exists(LocalTime startAt) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM RESERVATION_TIME WHERE START_AT = ?",
                Integer.class,
                startAt
        );
        return count != null && count > 0;
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM RESERVATION_TIME WHERE ID = ?", id);
    }

    public List<ReservationTime> available(LocalDate parse, long themeId) {
        String query="SELECT rt.id, rt.start_at " +
                "FROM reservation_time rt " +
                "LEFT JOIN reservation r ON rt.id = r.time_id AND r.date = ? AND r.theme_id = ? " +
                "WHERE r.id IS NULL";
        return jdbcTemplate.query(query,reservationTimeRowMapper,parse,themeId);
    }
}
