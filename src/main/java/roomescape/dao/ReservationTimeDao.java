package roomescape.dao;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimeDao {
    private static final RowMapper<ReservationTime> rowMapper = (rs, rowNum) -> {
        return new ReservationTime(rs.getLong("id")
                , LocalTime.parse(rs.getString("start_at"), DateTimeFormatter.ofPattern("HH:mm")));
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationTime> read() {
        String sql = "select id, start_at from reservation_time";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public ReservationTime findById(Long id) {
        String sql = "select id, start_at from reservation_time where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public ReservationTime create(ReservationTime time) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start_at", time.getStartAt());

        Long id = (long) simpleJdbcInsert.executeAndReturnKey(parameters);
        return new ReservationTime(id, time.getStartAt());
    }

    public void delete(Long id) {
        String sql = "delete from reservation_time where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
