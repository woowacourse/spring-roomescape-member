package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class H2ReservationTimeRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public H2ReservationTimeRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    private ReservationTime mapRowTime(final ResultSet rs, final int rowNum) throws SQLException {
        return new ReservationTime(
                rs.getLong("id"),
                LocalTime.parse(rs.getString("start_at"))
        );
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT * FROM reservation_time";

        return jdbcTemplate.query(sql, this::mapRowTime);
    }

    @Override
    public Optional<ReservationTime> findById(final Long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = ?";

        return jdbcTemplate.query(sql, this::mapRowTime, id)
                .stream()
                .findAny();
    }


    @Override
    public ReservationTime save(final ReservationTime time) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(time);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return time.assignId(id);
    }

    @Override
    public int deleteById(final Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }
}
