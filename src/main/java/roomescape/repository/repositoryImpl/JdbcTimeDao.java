package roomescape.repository.repositoryImpl;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.repository.TimeDao;

@Repository
public class JdbcTimeDao implements TimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<ReservationTime> timeRowMapper = (resultSet, rowNum) -> new ReservationTime(
            resultSet.getLong("id"),
            LocalTime.parse(resultSet.getString("start_at"))
    );

    public JdbcTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAll() {
        List<ReservationTime> reservationTimes = jdbcTemplate.query("SELECT * FROM reservation_time", timeRowMapper);
        return Collections.unmodifiableList(reservationTimes);
    }

    @Override
    public ReservationTime findById(long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, timeRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 예약 시간입니다.");
        }
    }

    @Override
    public long save(ReservationTime reservationTime) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(reservationTime);
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameterSource);
        return newId.longValue();
    }

    @Override
    public boolean existByTime(LocalTime time) {
        String sql = """
                SELECT count(*) 
                FROM reservation_time
                WHERE start_at = ?
                """;
        int count = jdbcTemplate.queryForObject(sql, Integer.class, time);
        return count > 0;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
    }
}
