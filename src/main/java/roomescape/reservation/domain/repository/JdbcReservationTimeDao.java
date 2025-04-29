package roomescape.reservation.domain.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.ReservationTime;

@Repository
public class JdbcReservationTimeDao implements ReservationTimeRepository {

    private final RowMapper<ReservationTime> rowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        LocalTime startAt = LocalTime.parse(rs.getString("start_at"));
        return new ReservationTime(id, startAt);
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long saveAndReturnId(ReservationTime time) {
        String sql = "INSERT INTO reservation_time (start_at) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, time.getStartAt().toString());
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public ReservationTime findById(Long id){
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

}
