package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String sql = """
                 SELECT 
                     r.id as reservation_id, 
                     r.name, 
                     r.date, 
                     rt.id as time_id, 
                     rt.start_at as time_value  
                 FROM reservation r
                 inner join reservation_time rt
                 ON r.time_id = rt.id;
                """;

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Long id = resultSet.getLong("reservation_id");
            String name = resultSet.getString("name");
            String date = resultSet.getString("date");

            Long timeId = resultSet.getLong("time_id");
            String timeValue = resultSet.getString("time_value");
            ReservationTime time = new ReservationTime(timeId, java.time.LocalTime.parse(timeValue));
            return new Reservation(id, name, java.time.LocalDate.parse(date), time);
        });
    }

    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES(?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});

            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setString(2, reservation.getDate().toString());
            preparedStatement.setLong(3, reservation.getTime().getId());

            return preparedStatement;
        }, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existByTimeId(Long timeId) {
        String sql = "SELECT count(*) FROM reservation where time_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, timeId);
        return count != null && count>0;
    }
}
