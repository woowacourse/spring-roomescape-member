package roomescape.reservation.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.reservation.entity.ReservationTimeEntity;

@Repository
public class ReservationTimeDao {
    private JdbcTemplate jdbcTemplate;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int count() {
        String sql = "select count(*) from reservation_time";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public int insert(ReservationTimeEntity reservationTimeEntity) {
        String sql = "insert into reservation_time (start_at) values (?)";
        return jdbcTemplate.update(sql, reservationTimeEntity.getStartAt());
    }

    public List<ReservationTimeEntity> findAllTimes() {
        String sql = "select id, start_at from reservation_time";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    return new ReservationTimeEntity(
                            resultSet.getLong("id"),
                            resultSet.getTime("start_at").toLocalTime()
                    );
                });
    }

    public int delete(Long id) {
        String sql = "delete from reservation_time where id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
