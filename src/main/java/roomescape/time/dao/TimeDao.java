package roomescape.time.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.time.entity.TimeEntity;

@Repository
public class TimeDao {
    private JdbcTemplate jdbcTemplate;

    public TimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insert(TimeEntity timeEntity) {
        String sql = "insert into reservation_time (start_at) values (?)";
        return jdbcTemplate.update(sql, timeEntity.getStartAt());
    }

}
