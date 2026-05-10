package roomescape.repository.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimeDao {

    private static final RowMapper<ReservationTime> reservationTimeRowMapper = (rs, rowNum) ->
            new ReservationTime(
                    rs.getLong("id"),
                    rs.getObject("start_at", LocalTime.class)
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public ReservationTime insert(ReservationTime reservationTime) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new ReservationTime(id, reservationTime.getStartAt());
    }

    public boolean existsByStartAt(LocalTime startAt) {
        String sql = "select count(1) from reservation_time where start_at = ?;";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, startAt);
        return count != null && count > 0;
    }

    public Optional<ReservationTime> selectById(Long id) {
        String sql = "select * from reservation_time where id = ?;";
        return jdbcTemplate.query(sql, reservationTimeRowMapper, id)
                .stream()
                .findFirst();
    }

    public ReservationTime getById(Long id) {
        String sql = "select * from reservation_time where id = ?;";
        return jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, id);
    }
    
    public List<ReservationTime> selectAll() {
        String sql = "select * from reservation_time order by start_at;";
        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    public int deleteById(Long id) {
        String sql = "delete from reservation_time where id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    public List<Long> selectReservedTimeIds(Long themeId, LocalDate date) {
        String sql = "select time_id " +
                "from reservation " +
                "where theme_id = ? and date = ?;";

        return jdbcTemplate.queryForList(sql, Long.class, themeId, date.toString());
    }
}
