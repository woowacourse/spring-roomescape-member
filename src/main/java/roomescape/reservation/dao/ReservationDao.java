package roomescape.reservation.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.reservation.model.ReservationEntity;

@Repository
public class ReservationDao {
    private JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int count() {
        String sql = "select count(*) from reservation";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public int insert(ReservationEntity reservationEntity) {
        String sql = "insert into reservation (name, date, time) values (?, ?, ?)";
        return jdbcTemplate.update(sql, reservationEntity.getName(), reservationEntity.getDate(), reservationEntity.getTime());
    }

    public List<ReservationEntity> findAllReservations() {
        String sql = "select id, name, date, time from reservation";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    return new ReservationEntity(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getDate("date").toLocalDate(),
                            resultSet.getTime("time").toLocalTime()
                    );
                });
    }

    public int delete(Long id) {
        String sql = "delete from reservation where id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
