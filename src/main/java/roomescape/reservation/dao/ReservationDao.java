package roomescape.reservation.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.reservation.model.Reservation;

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

    public int insert(Reservation reservation) {
        String sql = "insert into reservation (name, date, time) values (?, ?, ?)";
        return jdbcTemplate.update(sql, reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    public List<Reservation> findAllReservations() {
        String sql = "select id, name, date, time from reservation";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    return new Reservation(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getDate("date").toLocalDate(),
                            resultSet.getTime("time").toLocalTime()
                    );
                });
    }

}
