package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import javax.sql.DataSource;
import java.time.LocalTime;
import java.util.List;

@Repository
public class ReservationTimeDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationTimeDAO(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public ReservationTime insert(ReservationTime reservationTime) {
        final LocalTime startAt = reservationTime.getStartAt();

        final SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("start_at", startAt);

        final long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new ReservationTime(id, startAt);
    }

    public ReservationTime findById(Long id) {
        final String sql = "SELECT * FROM reservation_time WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, reservationTimeRowMapper(), id);
    }

    public List<ReservationTime> selectAll() {
        final String sql = "SELECT * FROM reservation_time";
        return jdbcTemplate.query(sql, reservationTimeRowMapper());
    }

    public void deleteById(Long id) {
        final String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return (resultSet, rowNum) -> new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime()
        );
    }
}
