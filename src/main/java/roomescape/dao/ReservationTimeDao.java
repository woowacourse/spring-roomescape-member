package roomescape.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
public class ReservationTimeDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(ReservationTime reservationTime) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(reservationTime);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return Objects.requireNonNull(id);
    }

    public List<ReservationTime> selectAll() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, this::rowMapper);
    }

    public ReservationTime selectById(Long id) {
        try {
            String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, this::rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private ReservationTime rowMapper(ResultSet resultSet, int rowNumber) throws SQLException {
        var reservationTime = new ReservationTime(resultSet.getString("start_at"));
        return new ReservationTime(resultSet.getLong("id"), reservationTime);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
