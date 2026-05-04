package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ReservationTime findById(Long id) {
        String sql = "select id, start_at from reservation_time where id = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
            return new ReservationTime(
                    resultSet.getLong("id"),
                    LocalTime.parse(resultSet.getString("start_at"))
            );
        }, id);
    }

    public List<ReservationTime> findAll(){
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, (resultSet, rowNum)-> {
            long id = resultSet.getLong("id");
            LocalTime startAt = LocalTime.parse(resultSet.getString("start_at"));

            return new ReservationTime(id, startAt);
        });
    }

    public ReservationTime save(ReservationTime reservationTime){
        String sql = "INSERT INTO reservation_time (start_at) VALUES(?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection-> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, reservationTime.getStartAt().toString());

            return preparedStatement;
        }, keyHolder);
        long newId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new ReservationTime(newId, reservationTime.getStartAt());
    }

    public void deleteById(Long id){
        String sql = "DELETE FROM reservation_time WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByStartAt(LocalTime startAt) {
        String sql = "Select count(*) from reservation_time where start_at = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, startAt.toString());
        return count != null && count >0;
    }
}
