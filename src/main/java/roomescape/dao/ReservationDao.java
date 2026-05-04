package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> actorRowMapper = (resultSet, rowNum) -> {
        ReservationTime time = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getString("time_value"));

        Reservation reservation = new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                resultSet.getString("date"),
                time);
        return reservation;
    };

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String sql = "SELECT\n" +
                "    r.id as reservation_id,\n" +
                "    r.name,\n" +
                "    r.date,\n" +
                "    t.id as time_id,\n" +
                "    t.start_at as time_value\n" +
                "FROM reservation as r\n" +
                "INNER JOIN reservation_time as t\n" +
                "  ON r.time_id = t.id";
        return jdbcTemplate.query(sql, actorRowMapper);
    }

    public Reservation findBy(Long id) {
        String sql = "SELECT\n" +
                "    r.id as reservation_id,\n" +
                "    r.name,\n" +
                "    r.date,\n" +
                "    t.id as time_id,\n" +
                "    t.start_at as time_value\n" +
                "FROM reservation as r\n" +
                "INNER JOIN reservation_time as t\n" +
                "  ON r.time_id = t.id\n" +
                "WHERE r.id = ?";
        return jdbcTemplate.queryForObject(sql, actorRowMapper, id);
    }

    public Long insert(Reservation reservation) {
        String sql = "INSERT INTO reservation(name, date, time_id) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(
                    sql,
                    new String[]{"id"});
            pstmt.setString(1, reservation.getName());
            pstmt.setString(2, reservation.getDate());
            pstmt.setLong(3, reservation.getTime().getId());
            return pstmt;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int delete(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }
}
